package com.ds.practice.problema.util

import java.io.{File, IOException, InputStream}
import java.util.Properties

import org.slf4j.LoggerFactory

class AppProperties @throws[IOException]
(propsFilePath: InputStream) extends Serializable {
  private val logger = LoggerFactory.getLogger(classOf[AppProperties])

  private val properties: Properties = new Properties
  try properties.load(propsFilePath)
  catch {
    case e: Exception =>
      logger.error("Application path=" + new File("").getAbsolutePath, e)
      throw e
  }

  def get(key: String): String = properties.getProperty(key).trim
}
