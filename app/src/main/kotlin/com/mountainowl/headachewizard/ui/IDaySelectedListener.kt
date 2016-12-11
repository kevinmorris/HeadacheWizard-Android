package com.mountainowl.headachewizard.ui

import org.joda.time.LocalDate

interface IDaySelectedListener {
    fun onDaySelected(date: LocalDate)
}