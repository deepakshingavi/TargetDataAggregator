package com.ds.practice.problema

import com.ds.practice.problema.http.HttpWrapper
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite

class EndToEndTest extends AnyFunSuite with MockFactory {

  var params: Map[String, String] = Map("param1" -> "val1")
  val sampleResponseStr: String = """{"from": 0, "took": 4, "next": [1.5656193, "ENSG00000133703-MONDO_0045024"], "data_version": "20.02", "query": {"sort": ["harmonic-sum.overall"], "search": null, "rna_expression_level": 0, "protein_expression_tissue": [], "scorevalue_types": ["overall"], "datatype": [], "fields": null, "format": "json", "facets_size": null, "disease": [], "protein_expression_level": 0, "tractability": [], "datastructure": "default", "facets": "false", "rna_expression_tissue": [], "target": [], "target_class": [], "cap_scores": true, "pathway": [], "size": 10}, "total": 7999050, "data": [{"association_score": {"overall": 1.0}}, {"association_score": {"overall": 1.0}}, {"association_score": {"overall": 1.0}}, {"association_score": {"overall": 1.0}}, {"association_score": {"overall": 1.0}}], "size": 10}""".stripMargin

  test("Complete flow test") {

    val cliReader = new Entry()
    val httpStub = stub[HttpWrapper]
    (httpStub.responseBody _).when(params).returns(sampleResponseStr)
    cliReader.validateAndSubmit("my_code_test -t ENSG00000157764".split(" "))

    //CHecking if the code is execute without error

  }

}
