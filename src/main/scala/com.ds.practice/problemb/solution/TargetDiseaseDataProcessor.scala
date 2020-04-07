package com.ds.practice.problemb.solution

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, SaveMode, SparkSession}


/**
 * All the data frame handling code goes here
 * @param spark - Spark session object
 * @param inputPath - JSON GZ path for reading the data
 * @param outPath - Path to save the csv ouput
 */
class TargetDiseaseDataProcessor (spark : SparkSession,inputPath: String,outPath: String){

  /**
   * Load the file and return the DataFrame
   * @return
   */
  def loadJsonGZFileInDF(): DataFrame = {
    spark.read.json(inputPath)
  }

  /**
   * Register the table into Spark SQl context with minimal columns
   * (target.id,disease.id,association_score)
   * @param df
   */
  def registerTableTargetDiseaseScore(df:DataFrame): Unit ={
    df.select(col("target.id").as("target_id"),
      col("disease.id").as("disease_id"),
      col("scores.association_score").as("association_score"))
      .createOrReplaceTempView("base_table")
  }

  /**
   * Computes the median of association score and add the output as a column
   * @return
   */
  def getMedianDf() : DataFrame = {
    spark.sql("select target_id,disease_id" +
      ",percentile_approx(association_score, 0.5) as median_score" +
      " from base_table group by target_id,disease_id")
      .toDF()
  }

  /**
   * Ranks the data according to association score for every target,disease and filters for only top 3 ranking scores.
   * @return
   */
  def top3AssociationScoreDf() : DataFrame = {
    spark.sql("select target_id,disease_id,collect_list(association_score) as asso_score_list from (select target_id,disease_id,association_score" +
      ", dense_rank() OVER (PARTITION BY target_id,disease_id ORDER BY association_score DESC) as rank" +
      " from base_table) tmp where rank < 4" +
      " group by target_id,disease_id")
      .toDF()
  }

  /**
   * Join the topScoresDf and medianDf and return the result
   * @param topScoresDf - Top 3 Score data frame
   * @param medianDf - Median score data frame
   * @return
   */
  def joinMediaAndAssociationScoresDf(topScoresDf: DataFrame,medianDf: DataFrame): DataFrame = {
    topScoresDf.join(medianDf, topScoresDf("target_id") === medianDf("target_id") && topScoresDf("disease_id") === medianDf("disease_id"))
      .withColumn("topAssociationScores", stringify(col("asso_score_list")))
      .drop(topScoresDf("target_id"))
      .drop(topScoresDf("disease_id"))
      .drop(topScoresDf("asso_score_list"))
      .orderBy("median_score")

  }

  /**
   * Data Frame utility method to Convert columns of Array type to String type.
   * @param c - Data Frame Column
   * @return
   */
  def stringify(c: Column) = concat(lit("["), concat_ws(",", c), lit("]"))

  def saveDfAsCsv(resultDf : DataFrame, overrideFlag : Boolean) : Unit = {
    if(overrideFlag) {
      resultDf
        .write
        .csv(outPath)
    } else {
      resultDf
        .write
          .mode(SaveMode.Overwrite)
        .csv(outPath)
    }
  }

  /**
   * Load only target and disease ids inot the Data Frame
   * @param df
   * @return
   */
  def getTargetDiseaseRelationDf(df : DataFrame): DataFrame = {
    df.select(col("target.id").as("target_id"),
      col("disease.id").as("disease_id"))
  }

  /**
   * 1. Self joins the target_disease_relation_df
   * 2. Drop the duplicate columns
   * 3. Drop the duplicate records
   * 4. Combines the Target-1 and Target-2 to a single column sorted array column.
   * 5. Drop the duplicate records
   * 6. Perform Aggregation : Group the Array(target-1,Target-2) with count
   * 7. Filter out count < 2
   * 8. Stringify the Spark array column to String so that it can be saved to CSV
   * @param target_disease_relation_df DataFrame with Target_id and disease_id
   * @return
   */
  def findTargetWithMultipleCommonDisease(target_disease_relation_df : DataFrame): DataFrame = {
    val resultDf = target_disease_relation_df
      .withColumnRenamed("target_id", "target_id_1")
      .withColumnRenamed("disease_id", "disease_id_1")
      .join(target_disease_relation_df
        .withColumnRenamed("target_id", "target_id_2")
        .withColumnRenamed("disease_id", "disease_id_2")
        , col("disease_id_1") === col("disease_id_2") &&
          col("target_id_1") =!= col("target_id_2")
      )
      .drop("disease_id_2")
      .withColumnRenamed("disease_id_1", "disease_id")
      .distinct()
      .select(array("target_id_1","target_id_2").as("targetArr"),col("disease_id"))
      .select(array_sort(col("targetArr")).as("targetSortedArr"),col("disease_id"))
      .distinct()
      .groupBy("targetSortedArr")
      .agg(count("disease_id").as("association_count"))
      .filter("association_count > 1")
        .withColumn("target_pair", stringify(col("targetSortedArr")))
        .drop("targetSortedArr")
    resultDf
  }

}