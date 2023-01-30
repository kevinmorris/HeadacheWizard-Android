package pro.kevinmorris.headachewizard.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.joda.time.LocalDate
import pro.kevinmorris.headachewizard.model.DataManager


class CalendarViewModel(val date: LocalDate) : ViewModel() {

    private val _days : List<List<MutableStateFlow<Int?>>> = listOf(
        listOf(
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null)),
        listOf(
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null)),
        listOf(
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null)),
        listOf(
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null)),
        listOf(
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null)),
    )

    val days : List<List<StateFlow<Int?>>> = _days


    private val _headaches : List<List<MutableStateFlow<Int?>>> = listOf(
        listOf(
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null)),
        listOf(
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null)),
        listOf(
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null)),
        listOf(
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null)),
        listOf(
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null),
            MutableStateFlow(null)),
    )

    val headaches : List<List<StateFlow<Int?>>> = _headaches

    init {
        var current = LocalDate(date.year, date.monthOfYear, 1)
        var week = 0
        while(current.monthOfYear == date.monthOfYear) {
            val headacheValue = DataManager.instance.headache.getDate(current)?.toInt()

            current = current.plusDays(1)
            if(current.dayOfWeek == 0) {
                week += 1
            }
        }
    }
}