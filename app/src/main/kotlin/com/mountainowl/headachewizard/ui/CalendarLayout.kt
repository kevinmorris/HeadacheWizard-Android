package com.mountainowl.headachewizard.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import org.joda.time.LocalDate

class CalendarLayout(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var xDown: Float? = null
    var year: Int = 0
    var month: Int = 0

    var layoutCalendarCallback: ICalendarLayoutListener? = null

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean = when (event?.action) {
        MotionEvent.ACTION_DOWN -> {
            xDown = event?.x
            false
        }
        MotionEvent.ACTION_UP -> {
            val xUp = event?.x
            val xDownLocal = xDown
            if (xDownLocal != null && xUp != null && Math.abs(xDownLocal - xUp) > 200) {

                val startDate = LocalDate(year, month, 1)

                if((xUp - xDownLocal) > 0) {
                    val previousMonth = startDate.minusMonths(1)
                    layoutCalendarCallback?.layoutCalendar(previousMonth.monthOfYear, previousMonth.year)
                } else {
                    val nextMonth = startDate.plusMonths(1)
                    layoutCalendarCallback?.layoutCalendar(nextMonth.monthOfYear, nextMonth.year)
                }
                
                true
            } else {
                false
            }
        }
        else -> false
    }

    interface ICalendarLayoutListener {
        fun layoutCalendar(month: Int, year: Int)
    }
}