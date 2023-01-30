package pro.kevinmorris.headachewizard.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.joda.time.LocalDate
import pro.kevinmorris.headachewizard.CorrelationCoefficientTransformEngine
import pro.kevinmorris.headachewizard.PearsonCorrelationEngine
import java.util.*

class Factor(val id: Long, val name: String) {
    private val data: SortedMap<LocalDate, Double>

    private val _r: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val r : StateFlow<Double> = _r

    val entryCount: Int
        get() = this.data.size

    init {
        data = TreeMap<LocalDate, Double>()
    }

    operator fun get(date: LocalDate): Double? {
        val datum = this.data[date]
        return datum
    }

    fun set(date: LocalDate, fValue: Double, headache : Headache) {
        this.data[date] = fValue
        evaluateCorrelationParameters(headache)
    }

    fun allDates(): Set<LocalDate> = data.keys

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
            val hValue = headache[dateKey] ?: 0.0

            n += 1
            sumF += fValue
            sumH += hValue
            sumFH += fValue * hValue
            sumFSquared += fValue * fValue
            sumHSquared += hValue * hValue

            allZeroFactorValue = allZeroFactorValue && fValue == 0.0
            allZeroHeadacheValue = allZeroHeadacheValue && hValue == 0.0
        }

        val r: Double = if (allZeroFactorValue || allZeroHeadacheValue) { //no data for either factor or headache
            0.0
        } else if(arrayOf(-sumH, sumH).contains(n.toDouble())) { //if headache data has no variance
            sumF / n * if (sumH >= 0) 1 else -1
        } else if(arrayOf(-sumF, sumF).contains(n.toDouble())) { //if factor data has no variance
            sumH / n * if (sumF >= 0) 1 else -1
        } else { //use pearson correlation coefficient
            CorrelationCoefficientTransformEngine.transform(
                PearsonCorrelationEngine.run(n, sumF, sumH, sumFH, sumFSquared, sumHSquared)
            )
        }

        this._r.value = r.coerceIn(-1.0, 1.0)
    }

    override fun toString(): String {
        return name
    }
}
