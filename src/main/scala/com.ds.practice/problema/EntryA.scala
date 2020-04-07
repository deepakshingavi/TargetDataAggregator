package com.ds.practice.problema

import java.io.FileInputStream
import java.util.concurrent.{BlockingQueue, Executors, LinkedBlockingQueue}

import com.ds.practice.problema.consumer.{RequestConsumer, ScoreAggConsumer}
import com.ds.practice.problema.http.HttpWrapper
import com.ds.practice.problema.model.ScoreAggPayload
import com.ds.practice.problema.util.{AppProperties, Constant}

import scala.io.StdIn


class EntryA() {

  val props = new AppProperties(new FileInputStream("src/main/resources/app.properties"))

  /* Possible CLI parameters and there mapping to URl params*/
  val possibleParamsMap = Map("exit" -> "", "--test" -> "", "-t" -> "target", "-d" -> "disease")

  /* Queue which manages request handling with URL parameters */
  val urlReqQueue: BlockingQueue[Map[String, String]] = new LinkedBlockingQueue()

  /* Queue which manages processed request data */
  val associationScoreQueue: BlockingQueue[ScoreAggPayload] = new LinkedBlockingQueue()

  /* Allocating half CPUs to URL request Processor */
  val coresForHttpReq: Int = props.get(Constant.TOTAL_NO_OF_CORES).toInt / 2

  /* Allocating half CPUs to calcualting the min,max,avg,std-dev Processor */
  val coresForAggregation: Int = props.get(Constant.TOTAL_NO_OF_CORES).toInt - coresForHttpReq

  /* Thread pool for consuming from Request queue, fetching the data from URL, parsing JSON data and pushing list of scores to the queue*/
  val httpRequestPool = Executors.newFixedThreadPool(coresForHttpReq)

  for (_ <- 1 to coresForHttpReq) {
    httpRequestPool.submit(new RequestConsumer(urlReqQueue, associationScoreQueue, new HttpWrapper(props.get(Constant.FILTER_API_URL))))
  }

  /* Thread pool for consuming from Score queue and performing and displaying the calculations*/
  val aggregatorPool = Executors.newFixedThreadPool(coresForAggregation)
  for (_ <- 1 to coresForAggregation) {
    aggregatorPool.submit(new ScoreAggConsumer(associationScoreQueue))
  }

  /**
   * Validates the incoming cmd and add the computed Request info to Request queue
   * @param input CLI input
   */
  def validateAndSubmit(input: Array[String]): Unit = {
    if (input.isEmpty || input.length < 2) {
      println(s"Insufficient input!")
      return
    }
    val param0 = input(0).toLowerCase
    val param1 = input(1).toLowerCase

    if (!"my_code_test".equalsIgnoreCase(param0)) {
      println(s"Invalid input= '$param0'")
      return
    }

    if (!possibleParamsMap.keySet.contains(param1)) {
      println(s"Invalid input= '$param1'")
      return
    }

    param1 match {
      case "exit" =>
        httpRequestPool.shutdownNow()
        aggregatorPool.shutdownNow()
        println("Program terminated !!!")
        System.exit(0)
      case "-t" | "-d" =>
        validateAndSubmitReq(input, param1)
      case "--test" =>
        urlReqQueue.add(Map(possibleParamsMap("-t") -> "ENSG00000157764"))
        urlReqQueue.add(Map(possibleParamsMap("-d") -> "EFO_0002422"))
        urlReqQueue.add(Map(possibleParamsMap("-d") -> "EFO_0000616"))
      case _ => println(s"Invalid input = '$param1'")
    }
  }

  /**
   * Validates and submits target(-t) and disease(-d) cmds only
   * @param input
   * @param param1
   */
  private def validateAndSubmitReq(input: Array[String], param1: String): Unit = {
    if (input.length < 3) {
      println(s"Insufficient input")
      return
    }
    urlReqQueue.add(Map(possibleParamsMap(param1) -> input(2)))
  }
}


object EntryA {

  /**
   * Entry point for starting the CLI
   * Inorder to exits you can my_code_test exit
   * @param args
   */
  def main(args: Array[String]): Unit = {
    val reader = new EntryA()
    while (true) {
      print(">>> ")
      val input = StdIn.readLine().split(" ")
      reader.validateAndSubmit(input)
    }
  }


}
