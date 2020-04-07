package com.ds.practice.problema

import java.io.ByteArrayOutputStream
import java.util.concurrent.LinkedBlockingQueue

import com.ds.practice.problema.consumer.ScoreAggConsumer
import com.ds.practice.problema.model.ScoreAggPayload
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite


class ScoreAggConsumerTest extends AnyFunSuite with BeforeAndAfterAll{

  test("Score Aggregation console output test") {
    val queue = new LinkedBlockingQueue[ScoreAggPayload]()
    val consumer = new ScoreAggConsumer(queue)

    val out = new ByteArrayOutputStream()
    Console.withOut(out)(consumer.consume(ScoreAggPayload(Map("target"->"ABC"),List(1.0,1.0))))
    assertResult("Map(target -> ABC) max=1.0 min=1.0 avg=1.0 std-dev=0.0\n")(out.toString)
  }

  test("Score Aggregation console output test 2") {
    val queue = new LinkedBlockingQueue[ScoreAggPayload]()
    val consumer = new ScoreAggConsumer(queue)

    val out = new ByteArrayOutputStream()
    Console.withOut(out)(consumer.consume(ScoreAggPayload(Map("target"->"ABC"),List(1.0,2.0,3.0))))
    assertResult("Map(target -> ABC) max=3.0 min=1.0 avg=2.0 std-dev=0.82\n")(out.toString)
  }

  test("Score Aggregation console output test 3") {
    val queue = new LinkedBlockingQueue[ScoreAggPayload]()
    val consumer = new ScoreAggConsumer(queue)

    val out = new ByteArrayOutputStream()
    Console.withOut(out)(consumer.consume(ScoreAggPayload(Map("target"->"ABC"),List(-2.0,0.0,2.0))))
    assertResult("Map(target -> ABC) max=2.0 min=-2.0 avg=0.0 std-dev=1.63\n")(out.toString)
  }

  test("Score Aggregation console output test 4") {
    val queue = new LinkedBlockingQueue[ScoreAggPayload]()
    val consumer = new ScoreAggConsumer(queue)

    val out = new ByteArrayOutputStream()
    Console.withOut(out)(consumer.consume(ScoreAggPayload(Map("target"->"ABC"),List(2,2,3,3,4,4,5,5))))
    assertResult("Map(target -> ABC) max=5.0 min=2.0 avg=3.5 std-dev=1.12\n")(out.toString)
  }
}


