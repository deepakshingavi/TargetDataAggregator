package com.ds.practice.problema.http

import scalaj.http.Http


/**
 * All the HTTP request handling and submission code goes here.
 */
class HttpWrapper(baseUrl:String) {

  /**
   * Submit the HTTP request and gives back the response body
   * @param params URL parameters
   * @return
   */
  def responseBody(params : Map[String,String]): String = {
    Http(baseUrl).params(params).asString.body
  }

}
