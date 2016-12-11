package com.mountainowl.headachewizard.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout

class EditDailyEntryFragmentLayout(context: Context?, attrs: AttributeSet?) :
        RelativeLayout(context, attrs) {

    lateinit var fragment: EditDailyEntryFragment

    private var xDown: Float? = null

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean = when (event?.action) {
        MotionEvent.ACTION_DOWN -> {
            xDown = event?.x
            false
        }
        MotionEvent.ACTION_UP -> {
            val xUp = event?.x
            val xDownLocal = xDown
            if (xDownLocal != null && xUp != null && Math.abs(xDownLocal - xUp) > 200) {

                if((xUp - xDownLocal) > 0) {
                    val previousDay = fragment.date.minusDays(1)
                    fragment.initializeData(previousDay)
                } else {
                    val nextDay = fragment.date.plusDays(1)
                    fragment.initializeData(nextDay)
                }

                true
            } else {
                false
            }
        }
        else -> false
    }
}