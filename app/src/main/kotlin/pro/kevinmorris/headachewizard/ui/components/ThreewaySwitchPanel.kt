package pro.kevinmorris.headachewizard.ui.components

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView

abstract class ThreewaySwitchPanel(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs), IThreewaySwitchListener {

    protected var switchControl: ThreewaySwitch? = null
    protected var switchLabel: TextView? = null

    var rowPosition: Int = 0
        set(rowPosition) {
            field = rowPosition
            if (switchControl != null) {
                switchControl!!.rowPosition = this.rowPosition
            }
        }

    fun get(): Double? {
        return switchControl!!.progress.toDouble() - 1
    }

    fun set(setting: Double?) {
        if (switchControl != null) {
            val progress = if (setting == null) 1 else Math.floor(setting).toInt() + 1
            switchControl!!.progress = progress
            if (progress == 0) {
                switchControl!!.onProgressChanged(switchControl!!, progress, false)
            }
            onSwitchChangedByUser(progress, this.rowPosition)
        }
    }

    fun addObserver(observer: IThreewaySwitchListener) {
        switchControl!!.addObserver(observer)
    }

    abstract override fun onSwitchChangedByUser(progress: Int, rowPosition: Int)

    abstract override fun onSwitchTouchUp(progress: Int, rowPosition: Int)
}
