package pro.kevinmorris.headachewizard.viewmodel

import androidx.lifecycle.ViewModel
import org.joda.time.DateTime


class MainViewModel : ViewModel() {

    val navState = NavigationController.navState

    fun editFactors() {
        NavigationController.editFactors()
    }

    fun editToday() {
        NavigationController.editDay(DateTime.now())
    }

    fun showCalendar() {
        NavigationController.showCalendar()
    }
}