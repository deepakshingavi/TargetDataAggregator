package com.ds.practice.problema

import java.util.concurrent.LinkedBlockingQueue

import com.ds.practice.problema.consumer.RequestConsumer
import com.ds.practice.problema.http.HttpWrapper
import com.ds.practice.problema.model.ScoreAggPayload
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite

class RequestConsumerTest extends AnyFunSuite
   with MockFactory {
  val sampleResponseStr: String = """{"from": 0, "took": 4, "next": [1.5656193, "ENSG00000133703-MONDO_0045024"], "data_version": "20.02", "query": {"sort": ["harmonic-sum.overall"], "search": null, "rna_expression_level": 0, "protein_expression_tissue": [], "scorevalue_types": ["overall"], "datatype": [], "fields": null, "format": "json", "facets_size": null, "disease": [], "protein_expression_level": 0, "tractability": [], "datastructure": "default", "facets": "false", "rna_expression_tissue": [], "target": [], "target_class": [], "cap_scores": true, "pathway": [], "size": 10}, "total": 7999050, "data": [{"association_score": {"overall": 1.0}}, {"association_score": {"overall": 1.0}}, {"association_score": {"overall": 1.0}}, {"association_score": {"overall": 1.0}}, {"association_score": {"overall": 1.0}}], "size": 10}""".stripMargin

  test("test request consumer") {
    val input = mock[LinkedBlockingQueue[Map[String,String]]]
    val output = mock[LinkedBlockingQueue[ScoreAggPayload]]
    val httpStub = stub[HttpWrapper]
    val params = Map("param1" -> "value1")

    val consumer  = new RequestConsumer(input,output,httpStub)

    (httpStub.responseBody _).when(params).returns(sampleResponseStr)
    (output.put _).expects(ScoreAggPayload(params,List(1.0, 1.0, 1.0, 1.0, 1.0)))

    consumer.consume(params)




  }

}

