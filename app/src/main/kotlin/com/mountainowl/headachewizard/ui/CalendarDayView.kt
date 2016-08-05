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
            dayField.setText(this.date!!.dayOfMonth + "")
        }

    private val headacheDataExists: Boolean = false
    private val width: Int = 0
    private val height: Int = 0

    init {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.calendar_day_view, this, true)
    }

    fun setHeadacheData(headacheData: Double?) {

        if (headacheData != null) {

            val headacheImage = findViewById(R.id.day_headache_image) as ImageView

            if (headacheData === -1) {
                headacheImage.setImageDrawable(resources.getDrawable(R.drawable.no_headache))
            } else if (headacheData === 0) {
                headacheImage.setImageDrawable(resources.getDrawable(R.drawable.maybe_headache))
            } else if (headacheData === 1) {
                headacheImage.setImageDrawable(resources.getDrawable(R.drawable.headache))
            } else {
                throw IllegalArgumentException(headacheData + "")
            }
        }
    }
}
