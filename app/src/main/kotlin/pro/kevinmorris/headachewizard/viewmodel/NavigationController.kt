package pro.kevinmorris.headachewizard.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.joda.time.DateTime


object NavigationController {

    private val _navState = MutableStateFlow<NavigationState>(
        NavigationState.Calendar(DateTime.now())
    )

    val navState : StateFlow<NavigationState> = _navState

    fun showPrev() {
        _navState.update { prevState ->
            when(prevState) {
                is NavigationState.EditDay -> NavigationState.EditDay(
                    prevState.date.minusDays(1)
                )

                is NavigationState.Calendar -> NavigationState.Calendar(
                    prevState.date.minusMonths(1)
                )

                NavigationState.EditFactors -> NavigationState.Calendar(DateTime.now())
            }
        }
    }

    fun showNext() {
        _navState.update { prevState ->
            when(prevState) {

                is NavigationState.EditDay -> NavigationState.EditDay(
                    prevState.date.plusDays(1)
                )

                is NavigationState.Calendar -> NavigationState.Calendar(
                    prevState.date.plusMonths(1)
                )

                NavigationState.EditFactors -> NavigationState.Calendar(DateTime.now())
            }
        }
    }

    fun submitEditDay() {
        _navState.update { prevState ->
            when(prevState) {

                //This case is the only one that should ever occur
                is NavigationState.EditDay -> NavigationState.Calendar(prevState.date)

                is NavigationState.Calendar -> NavigationState.Calendar(DateTime.now())
                NavigationState.EditFactors -> NavigationState.Calendar(DateTime.now())
            }
        }
    }

    fun submitFactors() {
        _navState.update { NavigationState.Calendar(DateTime.now()) }
    }
}