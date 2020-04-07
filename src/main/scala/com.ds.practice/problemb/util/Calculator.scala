package com.ds.practice.problemb.util

object Calculator {

  def scoreStandardDeviation(associationScore: List[Double], avg: Double): Double = {
    BigDecimal(math.sqrt(associationScore.map(a => math.pow(a - avg, 2)).sum / associationScore.size)).setScale(2,BigDecimal.RoundingMode.HALF_UP).toDouble
  }

  def scoreAverage(associationScore: List[Double]): Double = {
    associationScore.sum / associationScore.length
  }

}
