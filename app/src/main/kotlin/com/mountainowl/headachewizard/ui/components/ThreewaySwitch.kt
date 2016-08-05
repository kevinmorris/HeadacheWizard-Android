package com.mountainowl.headachewizard.ui.components

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.mountainowl.headachewizard.R

import java.util.ArrayList

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

    val observer: List<IThreewaySwitchListener>
        get() = observers

    fun addObserver(observer: IThreewaySwitchListener) {
        this.observers.add(observer)
    }
}
