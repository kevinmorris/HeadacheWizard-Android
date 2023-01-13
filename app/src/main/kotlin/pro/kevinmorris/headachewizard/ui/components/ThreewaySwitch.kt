package pro.kevinmorris.headachewizard.ui.components

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.widget.AppCompatSeekBar
import pro.kevinmorris.headachewizard.R
import pro.kevinmorris.headachewizard.util.ThreewaySwitchChanged

class ThreewaySwitch(context: Context, attrs: AttributeSet) : AppCompatSeekBar(context, attrs), OnSeekBarChangeListener {

    private var styledAttributes: TypedArray
    var threewaySwitchChanged : ThreewaySwitchChanged? = null

    private val leftBackground : Drawable?
    private val centerBackground : Drawable?
    private val rightBackground : Drawable?

    init {
        styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ThreewaySwitch)

        leftBackground = styledAttributes.getDrawable(R.styleable.ThreewaySwitch_leftBackground)
        centerBackground = styledAttributes.getDrawable(R.styleable.ThreewaySwitch_centerBackground)
        rightBackground = styledAttributes.getDrawable(R.styleable.ThreewaySwitch_rightBackground)

        setOnSeekBarChangeListener(this)
    }

    fun set(progress: Int) {
        this.progress = progress
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

        val r = progressDrawable.copyBounds()

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
}
