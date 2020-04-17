package com.ds.practice.problemb

import com.ds.practice.problemb.solution.{FirstPartSolution, SecondPartSolution, TargetDiseaseDataProcessor}
import org.apache.spark.sql.{AnalysisException, SparkSession}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

/**
 * Due to SBT test failure had to shift all the Spark related test in a single file
 */
class TargetDiseaseDataProcessorATest extends AnyFunSuite
  with BeforeAndAfterAll{
  var sparkSession: SparkSession = _

  override protected def beforeAll(): Unit = {
    sparkSession = SparkSession.builder()
      .master("local[1]")
      .getOrCreate()

  }

  override protected def afterAll(): Unit = {
    sparkSession.close()
  }

  test("not existing file") {
    assertThrows[AnalysisException] {
      val sol = initSecond("src/test/resources/notExist.json")
      sol.loadAndProcess()
    }
  }

  test("empty file") {
    val sol = initSecond("src/test/resources/empty.json")
    val df = sol.loadAndProcess()
    assert(df.isEmpty)
  }

  test("invalid json file") {
    assertThrows[AnalysisException] {
      val sol = initSecond("src/test/resources/invalid.json")
      sol.loadAndProcess()
    }
  }

  test("single record") {
    val sol = initSecond("src/test/resources/single1.json")
    val df = sol.loadAndProcess()
    assert(df.isEmpty)
  }

  test("2 target with same disease") {
    val sol = initSecond("src/test/resources/targetsSameDisease2.json")
    val df = sol.loadAndProcess()

    assert(df.count()==1)
    val row = df.collectAsList().get(0)

    assert(row.getAs[Long](0) > 1)
    assertResult("[Target-1,Target-2]")(row.getAs[String](1))
  }

  test("Mix records") {
    val sol = initSecond("src/test/resources/mixRecordsBSecondInput.json")
    val df = sol.loadAndProcess()

    assert(df.count()==2)
    val resultMap : Map[String,Long] = df.collect().map(r => (r.getString(1),r.getLong(0)) ).toMap

    assert(resultMap.contains("[Target-1,Target-2]"))
    assert(resultMap.contains("[Target-30,Target-31]"))
    assertResult(2)(resultMap("[Target-1,Target-2]"))
    assertResult(3)(resultMap("[Target-30,Target-31]"))

  }

  test("test simple target disease association cases") {
    val sol = initFirst("src/test/resources/sampleBFirstInput.json")
    val df = sol.loadAndProcess()
    val row = df.collectAsList().get(0)


    assertResult("ENSG00000140859")(row.getAs[String](0))
    assertResult("disease-1")(row.getAs[String](1))
    assertResult(6.0)(row.getAs[Double](2))
    assertResult("[11.0,10.0,9.0]")(row.getAs[String](3))

  }

  test("multiple records with same ranks ") {
    val sol = initFirst("src/test/resources/simple1B.json")
    val joinedDf = sol.loadAndProcess()
    joinedDf.show(20, false)
    val row = joinedDf.collectAsList().get(0)

    assertResult("Target-1")(row.getAs[String](0))
    assertResult("disease-1")(row.getAs[String](1))
    assertResult(6.0)(row.getAs[Double](2))
    assertResult("[10.0,10.0,9.0,9.0,8.0]")(row.getAs[String](3))

  }

  private def initFirst(input:String,output:String = "") = {
    val processor = new TargetDiseaseDataProcessor(sparkSession, input, output)
    val solution = new FirstPartSolution(sparkSession, processor)
    solution
  }

  private def initSecond(input:String,output:String = ""): SecondPartSolution = {
    val processor = new TargetDiseaseDataProcessor(sparkSession, input, output)
    val solution = new SecondPartSolution(sparkSession, processor)
    solution
  }

}
