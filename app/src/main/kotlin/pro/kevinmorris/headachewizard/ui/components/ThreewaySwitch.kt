package pro.kevinmorris.headachewizard.ui.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.widget.AppCompatSeekBar
import pro.kevinmorris.headachewizard.R
import pro.kevinmorris.headachewizard.util.ThreewaySwitchChanged

class ThreewaySwitch(context: Context, attrs: AttributeSet) : AppCompatSeekBar(context, attrs), OnSeekBarChangeListener {

    private var styledAttributes: TypedArray
    var threewaySwitchChanged : ThreewaySwitchChanged? = null

    init {
        styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.threeWaySwitch)
        setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

        val r = progressDrawable.copyBounds()

        val leftBackground = styledAttributes.getDrawable(R.styleable.threeWaySwitch_leftBackground)
        val centerBackground = styledAttributes.getDrawable(R.styleable.threeWaySwitch_centerBackground)
        val rightBackground = styledAttributes.getDrawable(R.styleable.threeWaySwitch_rightBackground)

        when (progress) {
            0 -> progressDrawable = leftBackground
            1 -> progressDrawable = centerBackground
            2 -> progressDrawable = rightBackground
        }

        progressDrawable.bounds = r
        if (fromUser) {
            threewaySwitchChanged?.also { it.invoke(progress) }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val result = super.onTouchEvent(event)

//        if(event.actionMasked == MotionEvent.ACTION_UP) {
//            observers.forEach { it.onSwitchTouchUp(progress, rowPosition) }
//        }

        return result
    }
}
