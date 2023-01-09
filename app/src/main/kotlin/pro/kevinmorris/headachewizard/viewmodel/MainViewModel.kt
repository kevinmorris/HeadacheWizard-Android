package pro.kevinmorris.headachewizard.viewmodel

import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {

    val navState = NavigationController.navState

    fun editFactors() {
        NavigationController.editFactors()
    }
}