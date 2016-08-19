package com.mountainowl.headachewizard.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.mountainowl.headachewizard.R

class FactorSwitchPanel(context: Context, attrs: AttributeSet) : ThreewaySwitchPanel(context, attrs) {

    init {

        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.factor_switch_panel, this)

        switchControl = findViewById(R.id.factor_switch_control) as ThreewaySwitch
        switchLabel = findViewById(R.id.factor_switch_label) as TextView

        this.addObserver(this)
    }

    override fun onSwitchChangedByUser(progress: Int, rowPosition: Int) {
        if (progress == 1) {
            switchLabel!!.text = "Not Sure"
        } else if (progress == 0) {
            switchLabel!!.text = "No"
        } else if (progress == 2) {
            switchLabel!!.text = "Yes"
        }
    }
}
