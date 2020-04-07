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
    logger.info("begin : process")
    val targetDiseaseDf = processor.getTargetDiseaseRelationDf(df)
    logger.info("Target Disease relation Data Frame loaded")
    val resultDf = processor.findTargetWithMultipleCommonDisease(targetDiseaseDf)
    logger.info("end : process")
    resultDf
  }

}
