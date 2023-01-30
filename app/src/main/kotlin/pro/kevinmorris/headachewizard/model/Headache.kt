package pro.kevinmorris.headachewizard.model

import org.joda.time.LocalDate
import java.util.*

class Headache(val data: SortedMap<LocalDate, Double> = TreeMap()) {

    operator fun get(date: LocalDate): Double? {
        val datum = data[date]
        return datum
    }

    fun allDates(): Set<LocalDate> = data.keys
}
