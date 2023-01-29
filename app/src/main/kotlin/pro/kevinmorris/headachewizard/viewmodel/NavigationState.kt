package pro.kevinmorris.headachewizard.viewmodel

import org.joda.time.LocalDate


sealed class NavigationState {
    data class Calendar(val date : LocalDate) : NavigationState()
    data class EditDay(val date : LocalDate) : NavigationState()
    object EditFactors : NavigationState()
}