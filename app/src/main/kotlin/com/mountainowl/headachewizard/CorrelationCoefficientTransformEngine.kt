package com.mountainowl.headachewizard

object CorrelationCoefficientTransformEngine {

    fun transform(coefficient: Double) = gaussianTransform(coefficient)

    private fun gaussianTransform(x: Double): Double {

        fun f(a: Double): Double = when {
            (a >= 0) -> -Math.pow(Math.E, -3*a*a) + 1
            else -> Math.pow(Math.E, -3*a*a) - 1
        }

        return f(x) / f(1.0)
    }
}