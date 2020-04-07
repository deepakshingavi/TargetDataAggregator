package com.ds.practice.problema

import java.io.ByteArrayOutputStream

import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite

class EntryTest extends AnyFunSuite with MockFactory{


  test("Verify invalid input start of cmd") {
    val entry = new Entry()
    entry.httpRequestPool.shutdownNow()
    entry.aggregatorPool.shutdownNow()

    while(!entry.httpRequestPool.isTerminated || !entry.aggregatorPool.isTerminated) {
      Thread.sleep(1000)
    }
    val out = new ByteArrayOutputStream()
    out.flush()
    Console.withOut(out)(entry.validateAndSubmit("my_code_test111 --test".split(" ")))
    assertResult("Invalid input= 'my_code_test111'\n")(out.toString)
    out.reset()

    Console.withOut(out)(entry.validateAndSubmit("".split(" ")))
    assertResult("Insufficient input!\n")(out.toString)
    out.reset()

    Console.withOut(out)(entry.validateAndSubmit("randomprefix -t".split(" ")))
    assertResult("Invalid input= 'randomprefix'\n")(out.toString)
    out.reset()

    Console.withOut(out)(entry.validateAndSubmit("my_code_test -a".split(" ")))
    assertResult("Invalid input= '-a'\n")(out.toString)
    out.reset()

  }

  test("Verify invalid cmd params") {
    val entry = new Entry()
    entry.httpRequestPool.shutdownNow()
    entry.aggregatorPool.shutdownNow()

    while(!entry.httpRequestPool.isTerminated || !entry.aggregatorPool.isTerminated) {
      Thread.sleep(1000)
    }
    val out = new ByteArrayOutputStream()
    Console.withOut(out)(entry.validateAndSubmit("my_code_test111 --test".split(" ")))
    assertResult("Invalid input= 'my_code_test111'\n")(out.toString)
  }

  test("test target cli input") {
    val entry = new Entry()
    entry.httpRequestPool.shutdownNow()
    entry.aggregatorPool.shutdownNow()
    while(!entry.httpRequestPool.isTerminated || !entry.aggregatorPool.isTerminated) {
      Thread.sleep(1000)
    }
    entry.validateAndSubmit("my_code_test -t ENSG00000157764".split(" "))
    assert(entry.urlReqQueue.size()==1)
    assertResult(Map("target"->"ENSG00000157764"))(entry.urlReqQueue.poll())
  }


  test("Test disease cli inpit") {
    val entry = new Entry()
    entry.httpRequestPool.shutdownNow()
    entry.aggregatorPool.shutdownNow()
    while(!entry.httpRequestPool.isTerminated || !entry.aggregatorPool.isTerminated) {
      Thread.sleep(1000)
    }
    entry.validateAndSubmit("my_code_test -d EFO_0002422".split(" "))
    assert(entry.urlReqQueue.size()==1)
    assertResult(Map("disease"->"EFO_0002422"))(entry.urlReqQueue.poll())
  }

  test("Verify --test cli input") {
    val entry = new Entry()
    entry.httpRequestPool.shutdownNow()
    entry.aggregatorPool.shutdownNow()
    while(!entry.httpRequestPool.isTerminated || !entry.aggregatorPool.isTerminated) {
      Thread.sleep(1000)
    }
    entry.validateAndSubmit("my_code_test --test".split(" "))
    assert(entry.urlReqQueue.size()==3)
    assert(entry.urlReqQueue.contains(Map("target"->"ENSG00000157764")))
    assert(entry.urlReqQueue.contains(Map("disease"->"EFO_0002422")))
    assert(entry.urlReqQueue.contains(Map("disease"->"EFO_0000616")))
  }

}
