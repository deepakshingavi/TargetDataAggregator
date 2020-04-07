package com.ds.practice.problemb.solution

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.LoggerFactory

class FirstPartSolution(spark: SparkSession, processor: TargetDiseaseDataProcessor) extends ASolution(spark, processor) {

  private val logger = LoggerFactory.getLogger("FirstPartSolutionCls")

  /**
   * Register/Caches short version of data in Spark SQL
   * Create 2 data frames (Median , Top Association score) and merges them into final Data Frame.
   *
   * @param df
   * @return
   */
  def process(df: DataFrame): DataFrame = {
    logger.info("begin : process")
    processor.registerTableTargetDiseaseScore(df)
    logger.info("Table register")
    val medianDf = processor.getMedianDf()
    logger.info("Media DF fetch complete")
    val top3ScoresDf = processor.top3AssociationScoreDf()
    logger.info("Topscores DF fetch complete")
    val resultDf = processor.joinMediaAndAssociationScoresDf(top3ScoresDf, medianDf)
    logger.info("Data frames join completed")
    logger.info("End  : process")
    resultDf
  }
}