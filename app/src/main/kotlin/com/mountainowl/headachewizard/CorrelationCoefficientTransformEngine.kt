package com.mountainowl.headachewizard

object CorrelationCoefficientTransformEngine {

    fun transform(coefficient: Double) = realRoot(coefficient, 3)

    private fun realRoot(radicand: Double, index: Int): Double {
        return when {
            (radicand >= 0) -> Math.pow(radicand, (1.0 / index))
            else -> -Math.pow(-radicand, (1.0 / index))
        }
    }
}