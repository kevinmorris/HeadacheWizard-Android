package com.mountainowl.headachewizard.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.mountainowl.headachewizard.R
import org.joda.time.LocalDate

class CalendarDayView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    var date: LocalDate? = null
        set(date) {
            field = date
            val dayField = findViewById(R.id.day_number) as TextView
            dayField.text = this.date!!.dayOfMonth.toString()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.calendar_day_view, this, true)
    }

    fun setHeadacheData(headacheData: Double?) {

        if (headacheData != null) {

            val headacheImage = findViewById(R.id.day_headache_image) as ImageView
            headacheImage.setImageDrawable(resources.getDrawable(when(headacheData) {
                -1.0 -> R.drawable.no_headache
                0.0 -> R.drawable.maybe_headache
                1.0 -> R.drawable.headache
                else -> throw IllegalArgumentException(headacheData.toString())
            }))
        }
    }
}
