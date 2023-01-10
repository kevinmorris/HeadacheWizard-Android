package pro.kevinmorris.headachewizard.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import pro.kevinmorris.headachewizard.R

class HeadacheSwitchPanel(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs)   {

    var switchControl: ThreewaySwitch
    var switchLabel: TextView
    lateinit var threewaySwitchChanged: ThreewaySwitchChanged

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.headache_switch_panel, this)

        switchControl = findViewById(R.id.headache_switch_control) as ThreewaySwitch
        switchLabel = findViewById(R.id.headache_switch_label) as TextView
    }

    fun threewaySwitchChanged(progress: Int) {
        when (progress) {
            1 -> {
                switchLabel.text = "Not Sure"
                switchLabel.setTextColor(resources.getColor(R.color.neutral_yellow))
            }
            0 -> {
                switchLabel.text = "No"
                switchLabel.setTextColor(resources.getColor(R.color.headache_no))
            }
            2 -> {
                switchLabel.text = "Yes"
                switchLabel.setTextColor(resources.getColor(R.color.headache_yes))
            }
        }
    }
}
