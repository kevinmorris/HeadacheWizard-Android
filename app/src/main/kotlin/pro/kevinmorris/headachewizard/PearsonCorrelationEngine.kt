package pro.kevinmorris.headachewizard

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
}
