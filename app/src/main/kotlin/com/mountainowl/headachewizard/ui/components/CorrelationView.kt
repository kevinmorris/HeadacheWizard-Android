package com.mountainowl.headachewizard.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.mountainowl.headachewizard.HeadacheWizardApplication
import com.mountainowl.headachewizard.R

class CorrelationView : View {

    var value: Double? = null
        set(value) {
            field = value
            this.invalidate()
        }

    private var paint: Paint? = null

    //Color Components of each of the base correlation colors
    private var greenSubRed: Int = 0
    private var greenSubGreen: Int = 0
    private var greenSubBlue: Int = 0

    private var yellowSubRed: Int = 0
    private var yellowSubGreen: Int = 0
    private var yellowSubBlue: Int = 0

    private var redSubRed: Int = 0
    private var redSubGreen: Int = 0
    private var redSubBlue: Int = 0

    private var gradient: RadialGradient? = null

    private var _width: Int = 0
    private var _height: Int = 0

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()
    }

    private fun initialize() {

        paint = Paint()

        val green = HeadacheWizardApplication.context.resources.getColor(R.color.negative_correlation_green)
        val yellow = HeadacheWizardApplication.context.resources.getColor(R.color.zero_correlation_yellow)
        val red = HeadacheWizardApplication.context.resources.getColor(R.color.positive_correlation_red)

        greenSubRed = green shr 16 and 0xFF
        greenSubGreen = green shr 8 and 0xFF
        greenSubBlue = green shr 0 and 0xFF

        yellowSubRed = yellow shr 16 and 0xFF
        yellowSubGreen = yellow shr 8 and 0xFF
        yellowSubBlue = yellow shr 0 and 0xFF

        redSubRed = red shr 16 and 0xFF
        redSubGreen = red shr 8 and 0xFF
        redSubBlue = red shr 0 and 0xFF

        paint!!.isDither = true
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val imageRadius = (Math.min(width, height) / 2).toFloat()

        val lighterHSV = FloatArray(3)
        val darkerHSV = FloatArray(3)

        val color = correlationColor(this.value)
        Color.colorToHSV(color, lighterHSV)
        Color.colorToHSV(color, darkerHSV)

        lighterHSV[2] *= 1.5f
        darkerHSV[2] *= 0.5f

        gradient = RadialGradient(
                width / 2.0f - imageRadius * 0.4f,
                height / 2.0f - imageRadius * 0.4f,
                (imageRadius * 1.4).toInt().toFloat(),
                intArrayOf(Color.HSVToColor(lighterHSV), color, Color.HSVToColor(darkerHSV)),
                floatArrayOf(0.1f, 0.6f, 1.0f),
                Shader.TileMode.CLAMP)

        paint!!.shader = gradient
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), imageRadius, paint!!)

    }

    fun correlationColor(value: Double?): Int {

        if (value == null || value == java.lang.Double.NaN || value < -1 || value > 1) {
            return Color.TRANSPARENT
        }

        val red: Long
        val green: Long
        val blue: Long

        //linear interpolation for color gradient
        if (value >= 0) { //positive correlation
            val t = value

            red = Math.round((1 - t) * yellowSubRed + t * redSubRed)
            green = Math.round((1 - t) * yellowSubGreen + t * redSubGreen)
            blue = Math.round((1 - t) * yellowSubBlue + t * redSubBlue)
        } else {
            val t = value + 1

            red = Math.round((1 - t) * greenSubRed + t * yellowSubRed)
            green = Math.round((1 - t) * greenSubGreen + t * yellowSubGreen)
            blue = Math.round((1 - t) * greenSubBlue + t * yellowSubBlue)
        }

        return Color.rgb(red.toInt(), green.toInt(), blue.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        _width = w
        _height = h
    }
}
