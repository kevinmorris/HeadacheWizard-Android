package pro.kevinmorris.headachewizard.viewmodel

import org.joda.time.DateTime


sealed class NavigationState {
    data class Calendar(val date : DateTime) : NavigationState()
    data class EditDay(val date : DateTime) : NavigationState()
    object EditFactors : NavigationState()
}