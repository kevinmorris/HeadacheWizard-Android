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
            dayField.text = if(this.date != null) this.date!!.dayOfMonth.toString() else ""
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.calendar_day_view, this, true)
    }

    fun setHeadacheData(headacheData: Double?) {

        val headacheImage = findViewById(R.id.day_headache_image) as ImageView
        headacheImage.setImageDrawable(when(headacheData) {
            -1.0 -> resources.getDrawable(R.drawable.no_headache, null)
            0.0 -> resources.getDrawable(R.drawable.maybe_headache, null)
            1.0 -> resources.getDrawable(R.drawable.headache, null)
            null -> null
            else -> throw IllegalArgumentException(headacheData.toString())
        })
    }
}
