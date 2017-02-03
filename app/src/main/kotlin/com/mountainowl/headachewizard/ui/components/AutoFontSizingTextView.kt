package com.mountainowl.headachewizard.ui.components

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.TextView


class AutoFontSizingTextView(context: Context?, attrs: AttributeSet?) : TextView(context, attrs) {

    val bounds = Rect()
    val availableFontSizes = listOf(30.0f, 22.0f, 14.0f)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val str = this.text.toString()

        for(fs in availableFontSizes) {
            textSize = fs
            val textPaint = this.paint
            textPaint.getTextBounds(str, 0, str.length, bounds)

            if((bounds.right - bounds.left) < (right - left)) {
                break
            }
        }
    }
}