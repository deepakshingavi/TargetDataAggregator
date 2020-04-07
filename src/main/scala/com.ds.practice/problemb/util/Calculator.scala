package com.ds.practice.problemb.util

object Calculator {

  def scoreStandardDeviation(associationScore: List[Double], avg: Double): Double = {
    math.sqrt(associationScore.map(a => math.pow(a - avg, 2)).sum / associationScore.size)
  }

  def scoreAverage(associationScore: List[Double]): Double = {
    associationScore.sum / associationScore.length
  }

}
