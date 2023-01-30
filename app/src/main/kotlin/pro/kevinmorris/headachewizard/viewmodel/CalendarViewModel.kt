package pro.kevinmorris.headachewizard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
import pro.kevinmorris.headachewizard.model.DataManager
import pro.kevinmorris.headachewizard.model.Headache


class CalendarViewModel(val d: LocalDate) : ViewModel() {

    private val _date : MutableStateFlow<LocalDate> = MutableStateFlow(d)
    val date : StateFlow<LocalDate> = _date

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
        viewModelScope.launch {
            date.collect {
                loadCalendarData(it, DataManager.instance.headache)
            }
        }
    }

    fun prevMonth() {
        _date.update { oldDate -> oldDate.minusMonths(1).withDayOfMonth(1) }
    }

    fun nextMonth() {
        _date.update { oldDate -> oldDate.plusMonths(1).withDayOfMonth(1) }
    }

    private fun loadCalendarData(date : LocalDate, headache: Headache) {

        var current = date.withDayOfMonth(1)
        for(week in 0..4) {
            for(day in 0..6) {
                if(day < current.dayOfWeek % 7 || current.monthOfYear != date.monthOfYear) {
                    _days[week][day].value = null
                    _headaches[week][day].value = null
                } else {
                    _days[week][day].value = current.dayOfMonth
                    _headaches[week][day].value = headache[current]?.toInt()
                    current = current.plusDays(1)
                }
            }
        }
    }
}