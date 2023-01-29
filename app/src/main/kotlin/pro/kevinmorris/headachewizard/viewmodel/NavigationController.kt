package pro.kevinmorris.headachewizard.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.joda.time.LocalDate


object NavigationController {

    private val _navState = MutableStateFlow<NavigationState>(
        NavigationState.Calendar(LocalDate.now())
    )

    val navState: StateFlow<NavigationState> = _navState

    fun showPrev() {
        _navState.update { prevState ->
            when (prevState) {
                is NavigationState.EditDay -> NavigationState.EditDay(
                    prevState.date.minusDays(1)
                )

                is NavigationState.Calendar -> NavigationState.Calendar(
                    prevState.date.minusMonths(1)
                )

                NavigationState.EditFactors -> NavigationState.Calendar(LocalDate.now())
            }
        }
    }

    fun showNext() {
        _navState.update { prevState ->
            when (prevState) {

                is NavigationState.EditDay -> NavigationState.EditDay(
                    prevState.date.plusDays(1)
                )

                is NavigationState.Calendar -> NavigationState.Calendar(
                    prevState.date.plusMonths(1)
                )

                NavigationState.EditFactors -> NavigationState.Calendar(LocalDate.now())
            }
        }
    }

    fun submitEditDay() {
        showCalendar()
    }

    fun submitFactors() {
        showCalendar()
    }

    fun showCalendar() {
        _navState.update { NavigationState.Calendar(LocalDate.now()) }
    }

    fun editDay(date: LocalDate) {
        _navState.update { NavigationState.EditDay(date) }
    }

    fun editFactors() {
        _navState.update { prevState ->
            NavigationState.EditFactors
        }
    }
}