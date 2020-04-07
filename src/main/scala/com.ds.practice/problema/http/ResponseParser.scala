package com.ds.practice.problema.http

import org.json4s.JsonAST.JDouble
import org.json4s.jackson.JsonMethods.parse

/**
 * All the HTTP resposne JSON parsking code goes here
 */
class ResponseParser {

  /**
   * Processes the JSON string gets all the overall values and returns them as a list
   * @param responseStr JSON string
   * @return
   */
  def getOverallValues(responseStr : String) : List[Double] = {
    parse(responseStr) \\ "overall" \\ classOf[JDouble]
  }

}
