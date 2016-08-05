package com.mountainowl.headachewizard

import org.apache.commons.math3.util.FastMath

object PearsonCorrelationEngine {

    fun run(n: Int,
            sumX: Double,
            sumY: Double,
            sumXY: Double,
            sumXSquared: Double,
            sumYSquared: Double): Double {

        val numerator = n * sumXY - sumX * sumY
        val denominator = Math.sqrt(n * sumXSquared - sumX * sumX) * Math.sqrt(n * sumYSquared - sumY * sumY)

        return numerator / denominator
    }

    fun run(n: Int,
            sumX: Double,
            sumY: Double,
            sumXY: Double,
            sumXSquared: Double,
            sumYSquared: Double,
            xi: Double,
            yi: Double): Double {

        val numerator = n * sumXY + (n - 1).toDouble() * xi * yi - sumX * sumY - yi * sumX - xi * sumY
        val denominator = FastMath.sqrt(n * sumXSquared + (n - 1).toDouble() * xi * xi - sumX * sumX - 2.0 * xi * sumX) * FastMath.sqrt(n * sumYSquared + (n - 1).toDouble() * yi * yi - sumY * sumY - 2.0 * yi * sumY)

        return numerator / denominator
    }

    fun calculateSum(x: Map<*, Double>): Double {

        var sum = 0.0
        for (aDouble in x.values) {
            sum += aDouble
        }

        return sum
    }

    fun calculateSumOfProducts(x: Map<*, Double>, y: Map<*, Double>): Double {

        var sum = 0.0
        for ((key, value) in x) {
            val yy = y[key]

            sum += value * yy!!
        }

        return sum
    }

    fun calculateSumOfSquares(x: Map<*, Double>): Double {

        var sum = 0.0
        for (value in x.values) {
            sum += value * value
        }

        return sum
    }

}
