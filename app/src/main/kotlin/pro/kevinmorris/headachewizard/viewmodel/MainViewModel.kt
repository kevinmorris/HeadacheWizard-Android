package pro.kevinmorris.headachewizard.viewmodel

import androidx.lifecycle.ViewModel
import org.joda.time.LocalDate


class MainViewModel : ViewModel() {

    val navState = NavigationController.navState

    fun editFactors() {
        NavigationController.editFactors()
    }

    fun editToday() {
        NavigationController.editDay(LocalDate.now())
    }

    fun showCalendar() {
        NavigationController.showCalendar()
    }
}