package com.ds.practice.problema.consumer

import java.util.concurrent.BlockingQueue

import com.ds.practice.problema.http.{HttpWrapper, ResponseParser}
import com.ds.practice.problema.model.ScoreAggPayload
import org.json4s.DefaultFormats
import org.slf4j.LoggerFactory

/**
 * Consumes from request info consumeQueue and
 * Produces the over all values and pushes them to produceQueue
 * @param consumeQueue HTTP Request queue
 * @param produceQueue Queue which has overall score list for every http request
 * @param http - HTTP Wrapper for handling HTTP request
 */
class RequestConsumer(consumeQueue: BlockingQueue[Map[String,String]],
                      produceQueue: BlockingQueue[ScoreAggPayload],
                      http: HttpWrapper) extends AConsumer[Map[String,String]](consumeQueue) {

  private val logger = LoggerFactory.getLogger("RequestConsumer")

  /* Implicit formatter JSON parsing*/
  implicit val formats: DefaultFormats.type = DefaultFormats

  /**
   *
   * @param params URL paramter e.g. target=ENSG00000157764 or disease=EFO_0002422
   */
  def consume(params: Map[String,String]): Unit = {
    val responseStr = http.responseBody(params)
    val overallValues : List[Double] = new ResponseParser().getOverallValues(responseStr)
    produceQueue.put(ScoreAggPayload(params,overallValues))
  }
}
