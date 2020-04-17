package com.ds.practice.problemb.solution

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.LoggerFactory

abstract class ASolution(spark:SparkSession,processor:TargetDiseaseDataProcessor) {
  private val logger = LoggerFactory.getLogger("ASolutionCls")

  def process(df: DataFrame): DataFrame

  /**
   * Loads the JSON GZ file,
   */
  def loadAndProcess(): DataFrame = {
    val df = processor.loadJsonGZFileInDF()
    if (df.isEmpty) {
      logger.warn("JSON file is empty !!!")
      df
    } else {
      process(df: DataFrame)
    }
  }

  /**
   * Saves the result to output path as CSV.
   * @param df
   */
  def saveFile(df: DataFrame): Unit = {
    processor.saveDfAsCsv(df, true)
  }
}
