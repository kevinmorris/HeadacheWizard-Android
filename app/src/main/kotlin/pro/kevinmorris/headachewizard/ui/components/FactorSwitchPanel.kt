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
            switchControl.onProgressChanged(switchControl, b.first, false)
        }

    init {

        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.factor_switch_panel, this)

        switchControl = (findViewById<ThreewaySwitch>(R.id.factor_switch_control)).apply {
            threewaySwitchChanged = ::threewaySwitchChanged
        }
        switchLabel = findViewById<TextView>(R.id.factor_switch_label)
    }

    private fun threewaySwitchChanged(progress : Int) {
        when (progress) {
            1 -> switchLabel.text = "Not Sure"
            0 -> switchLabel.text = "No"
            2 -> switchLabel.text = "Yes"
        }

        binding.second?.invoke(progress)
    }
}
