package pro.kevinmorris.headachewizard.ui.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import pro.kevinmorris.headachewizard.R
import java.util.*

class ThreewaySwitch(context: Context, attrs: AttributeSet) : SeekBar(context, attrs), OnSeekBarChangeListener {

    protected var styledAttributes: TypedArray
    var rowPosition: Int = 0
    private val observers: MutableList<IThreewaySwitchListener>

    init {
        styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.threeWaySwitch)
        this.observers = ArrayList<IThreewaySwitchListener>()
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
        if (fromUser && !observers.isEmpty()) {
            for (observer in observers) {
                observer.onSwitchChangedByUser(getProgress(), rowPosition)
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val result = super.onTouchEvent(event)

        if(event.actionMasked == MotionEvent.ACTION_UP) {
            observers.forEach { it.onSwitchTouchUp(progress, rowPosition) }
        }

        return result
    }

    fun addObserver(observer: IThreewaySwitchListener) {
        this.observers.add(observer)
    }
}
