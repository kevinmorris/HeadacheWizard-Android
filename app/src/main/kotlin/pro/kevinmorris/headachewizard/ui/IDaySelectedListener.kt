package pro.kevinmorris.headachewizard.ui

import org.joda.time.LocalDate

interface IDaySelectedListener {
    fun onDaySelected(date: LocalDate)
}