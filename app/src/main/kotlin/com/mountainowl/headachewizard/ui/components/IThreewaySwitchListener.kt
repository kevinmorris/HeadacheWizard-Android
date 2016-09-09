package com.mountainowl.headachewizard.ui.components

interface IThreewaySwitchListener {

    fun onSwitchChangedByUser(progress: Int, position: Int)
    fun onSwitchTouchUp(progress: Int, position: Int)
}
