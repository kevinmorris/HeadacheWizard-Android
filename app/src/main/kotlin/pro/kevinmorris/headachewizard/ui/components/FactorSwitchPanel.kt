package pro.kevinmorris.headachewizard.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import pro.kevinmorris.headachewizard.R

class FactorSwitchPanel(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var switchControl: ThreewaySwitch
    var switchLabel: TextView
    lateinit var threewaySwitchChanged: ThreewaySwitchChanged

    init {

        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.factor_switch_panel, this)

        switchControl = findViewById(R.id.factor_switch_control) as ThreewaySwitch
        switchLabel = findViewById(R.id.factor_switch_label) as TextView

        switchControl.threewaySwitchChanged = this::threewaySwitchChanged
    }

    fun threewaySwitchChanged(progress : Int) {
        when (progress) {
            1 -> switchLabel.text = "Not Sure"
            0 -> switchLabel.text = "No"
            2 -> switchLabel.text = "Yes"
        }

        threewaySwitchChanged.invoke(progress)
    }
}
