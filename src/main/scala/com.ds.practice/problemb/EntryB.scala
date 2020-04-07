package com.ds.practice.problemb

import com.ds.practice.problemb.solution.{ASolution, FirstPartSolution, SecondPartSolution, TargetDiseaseDataProcessor}
import org.apache.spark.sql.{AnalysisException, SparkSession}
import org.slf4j.LoggerFactory

object EntryB {

  private val logger = LoggerFactory.getLogger("FirstPartSolutionObj")

  /**
   * Entry point for calculating target,disease,association-score,median from the JSON data.
   *
   * @param args {input data file path}, {output data file path}
   */
  def main(args: Array[String]): Unit = {
    logger.info("Begin : First part solution")
    try {
      if(args.length !=3) {
        logger.error(s"Insufficient Program arguments!!! arg.length=${args.length}")
        return
      }
      val spark = SparkSession.builder.getOrCreate
      val processor = new TargetDiseaseDataProcessor(spark, args(0), args(1))

      val solution : ASolution = args(2).toLowerCase match   {
        case "first" => new FirstPartSolution(spark, processor)
        case "second" => new SecondPartSolution(spark, processor)
        case _ => {
          logger.error(s"""Invalid input="${args(2)}" try first or second !!!""")
          return
        }
      }

      val df = solution.loadAndProcess()
      solution.saveFile(df)
    } catch {
      case e : AnalysisException => {
        if(e.message.contains("already exists")) {
          logger.error(s"Output path cannot overwritten path=${args(1)}",e)
        } else if(e.message.contains("Path does not exist:")) {
          logger.error(s"Invalid input file path=${args(1)}",e)
        } else if(e.message.contains("_corrupt_record")) {
          logger.error(s"Corrupt input JSON file=${args(0)}",e)
        }
        else {
          logger.error(s"Unknown error",e)
        }
      }
    }
    logger.info("End  : First part solution")
  }

}