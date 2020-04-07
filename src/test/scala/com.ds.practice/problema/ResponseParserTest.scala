package com.ds.practice.problema

import com.ds.practice.problema.http.ResponseParser
import org.scalatest.funsuite.AnyFunSuite
class ResponseParserTest extends AnyFunSuite{

  test("Response Parser should return List of double") {
    val sampleResponseStr = """{"from": 0, "took": 4, "next": [1.5656193, "ENSG00000133703-MONDO_0045024"], "data_version": "20.02", "query": {"sort": ["harmonic-sum.overall"], "search": null, "rna_expression_level": 0, "protein_expression_tissue": [], "scorevalue_types": ["overall"], "datatype": [], "fields": null, "format": "json", "facets_size": null, "disease": [], "protein_expression_level": 0, "tractability": [], "datastructure": "default", "facets": "false", "rna_expression_tissue": [], "target": [], "target_class": [], "cap_scores": true, "pathway": [], "size": 10}, "total": 7999050, "data": [{"association_score": {"overall": 1.0}}, {"association_score": {"overall": 1.0}}, {"association_score": {"overall": 1.0}}, {"association_score": {"overall": 1.0}}, {"association_score": {"overall": 1.0}}], "size": 10}""".stripMargin
    val overallValues = new ResponseParser().getOverallValues(sampleResponseStr)

    assertResult(List(1.0, 1.0, 1.0, 1.0, 1.0))(overallValues)
  }

}
