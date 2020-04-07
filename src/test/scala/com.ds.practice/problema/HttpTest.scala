package com.ds.practice.problema

import java.io.FileInputStream

import com.ds.practice.problema.util.{AppProperties, Constant}
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JDouble
import org.json4s.jackson.JsonMethods.parse
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import scalaj.http.Http

class HttpTest extends AnyFunSuite
  with BeforeAndAfterAll {
  implicit val formats: DefaultFormats.type = DefaultFormats

  test("Http response and JSON parsing") {
    val apps = new AppProperties(new FileInputStream("src/test/resources/app.properties"))
    val responseStr = Http(apps.get(Constant.FILTER_API_URL).concat("?")).asString.body
    val overalls: List[Double] = (parse(responseStr) \\ "overall" \\ classOf[JDouble])
    assert(overalls.nonEmpty)
  }

}

case class temp(overall: String)
