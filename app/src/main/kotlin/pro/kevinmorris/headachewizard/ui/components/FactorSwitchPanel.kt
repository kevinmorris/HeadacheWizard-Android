package pro.kevinmorris.headachewizard.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import pro.kevinmorris.headachewizard.R
import pro.kevinmorris.headachewizard.util.ThreewaySwitchChanged

class FactorSwitchPanel(context: Context, attrs : AttributeSet) : LinearLayout(context, attrs) {

    var switchControl: ThreewaySwitch
    var switchLabel: TextView

    var binding : Pair<Int, ThreewaySwitchChanged?> = Pair(0, null)
        set(b) {
            field = b
            switchControl.set(b.first + 1)
            setLabel(b.first + 1)
        }

    init {

        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.factor_switch_panel, this)

        switchControl = (findViewById<ThreewaySwitch>(R.id.factor_switch_control)).apply {
            threewaySwitchChanged = ::threewaySwitchChanged
        }
        switchLabel = findViewById(R.id.factor_switch_label)
    }

    private fun threewaySwitchChanged(progress : Int) {
        binding.second?.invoke(progress - 1)
    }

    private fun setLabel(progress : Int) {
        when (progress) {
            0 -> switchLabel.text = "No"
            1 -> switchLabel.text = "Not Sure"
            2 -> switchLabel.text = "Yes"
        }
    }
}
