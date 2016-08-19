package com.mountainowl.headachewizard.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.mountainowl.headachewizard.R

class HeadacheSwitchPanel(context: Context, attrs: AttributeSet) : ThreewaySwitchPanel(context, attrs) {

    init {

        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.headache_switch_panel, this)

        switchControl = findViewById(R.id.headache_switch_control) as ThreewaySwitch
        switchLabel = findViewById(R.id.headache_switch_label) as TextView

        this.addObserver(this)
    }

    override fun onSwitchChangedByUser(progress: Int, rowPosition: Int) {
        if (progress == 1) {
            switchLabel!!.text = "Not Sure"
            switchLabel!!.setTextColor(resources.getColor(R.color.neutral_yellow))
        } else if (progress == 0) {
            switchLabel!!.text = "No"
            switchLabel!!.setTextColor(resources.getColor(R.color.headache_no))
        } else if (progress == 2) {
            switchLabel!!.text = "Yes"
            switchLabel!!.setTextColor(resources.getColor(R.color.headache_yes))
        }
    }


}
