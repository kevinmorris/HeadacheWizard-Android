package pro.kevinmorris.headachewizard.model

import pro.kevinmorris.headachewizard.CorrelationCoefficientTransformEngine
import pro.kevinmorris.headachewizard.PearsonCorrelationEngine
import org.joda.time.LocalDate
import java.util.*

class Factor(val id: Long, val name: String) {
    private val data: SortedMap<LocalDate, Double>

    var r: Double = 0.0
        private set(r) {
            field = Math.min(Math.max(r, -1.0), 1.0)
        }

    val entryCount: Int
        get() = this.data.size

    init {
        data = TreeMap<LocalDate, Double>()
    }

    fun getDate(date: LocalDate): Double? {
        val datum = this.data[date]
        return datum
    }

    fun allDates(): Set<LocalDate> = data.keys

    fun setDate(date: LocalDate, fValue: Double) {
        this.data.put(date, fValue)
    }

    fun evaluateCorrelationParameters(headache: Headache) {

        var sumF: Double = 0.0
        var sumH: Double = 0.0
        var sumFH: Double = 0.0
        var sumFSquared: Double = 0.0
        var sumHSquared: Double = 0.0

        var allZeroFactorValue = true
        var allZeroHeadacheValue = true

        var n = 0

        val dates = headache.allDates().intersect(allDates())

        for (dateKey in dates) {
            val fValue = data[dateKey] ?: 0.0
            val hValue = headache.getDate(dateKey) ?: 0.0

            n += 1
            sumF += fValue
            sumH += hValue
            sumFH += fValue * hValue
            sumFSquared += fValue * fValue
            sumHSquared += hValue * hValue

            allZeroFactorValue = allZeroFactorValue && fValue == 0.0
            allZeroHeadacheValue = allZeroHeadacheValue && hValue == 0.0
        }

        val r: Double
        if (allZeroFactorValue || allZeroHeadacheValue) { //no data for either factor or headache
            r = 0.0
        } else if(arrayOf(-sumH, sumH).contains(n.toDouble())) { //if headache data has no variance
            r = sumF / n * if (sumH >= 0) 1 else -1
        } else if(arrayOf(-sumF, sumF).contains(n.toDouble())) { //if factor data has no variance
            r = sumH / n * if (sumF >= 0) 1 else -1
        } else { //use pearson correlation coefficient
            r = CorrelationCoefficientTransformEngine.transform(
                    PearsonCorrelationEngine.run(n, sumF, sumH, sumFH, sumFSquared, sumHSquared)
            )
        }

        this.r = r
    }

    override fun toString(): String {
        return name
    }
}
