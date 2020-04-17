package com.ds.practice.problema.consumer

import java.util.concurrent.BlockingQueue

import com.ds.practice.problema.model.ScoreAggPayload
import com.ds.practice.problemb.util.Calculator._
import org.slf4j.LoggerFactory

/**
 * Consumer for calculating the agg on scores
 * @param consumeQueue Overall score queue
 */
class ScoreAggConsumer(consumeQueue:BlockingQueue[ScoreAggPayload]) extends AConsumer[ScoreAggPayload](consumeQueue) {

  private val logger = LoggerFactory.getLogger("RequestConsumer")

  /**
   * Consumes the score and prints the output
   * @param queueMsg - scores with Request meta info(needed to identify the request)
   */
  override def consume(queueMsg: ScoreAggPayload): Unit = {
    val associationScore = queueMsg.scores
    val avg = scoreAverage(associationScore)
    val stdDeviation = scoreStandardDeviation(associationScore, avg)
    val resultStr = s"${queueMsg.metaData} max=${associationScore.max} min=${associationScore.min} avg=$avg std-dev=$stdDeviation"
    println(resultStr)
    logger.info(resultStr)
  }


}
