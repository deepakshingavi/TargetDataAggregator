package com.ds.practice.problemb.solution

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.LoggerFactory

class SecondPartSolution(spark:SparkSession,processor:TargetDiseaseDataProcessor) extends ASolution(spark,processor) {


  private val logger = LoggerFactory.getLogger("SecondPartSolutionCls")


  /**
   * Performs sequence compute operations usinfg Spark Data Frame with Target pairs and saves output as CSV.
   * @param df - Loaded Data Frame
   */
  def process(df: DataFrame): DataFrame = {
    val start = System.currentTimeMillis()
    logger.info("begin : process")
    val targetDiseaseDf = processor.getTargetDiseaseRelationDf(df)
    val dfFetchedAt = System.currentTimeMillis()
    val resultDf = processor.findTargetWithMultipleCommonDisease(targetDiseaseDf)
    val end = System.currentTimeMillis()
    logger.info(s"end : process time1=${end-dfFetchedAt} and time2=${dfFetchedAt-start}")
    resultDf
  }

}
