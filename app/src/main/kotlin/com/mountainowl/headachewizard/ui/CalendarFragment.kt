package com.mountainowl.headachewizard.ui

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.mountainowl.headachewizard.R
import com.mountainowl.headachewizard.model.DataManager
import com.mountainowl.headachewizard.model.Headache
import org.joda.time.Duration
import org.joda.time.LocalDate

class CalendarFragment : Fragment() {

    private lateinit var daySelectedListener: IDaySelectedListener
    private var month: Int = 0
    private var year: Int = 0

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        this.daySelectedListener = activity as IDaySelectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_calendar, container, false)

        return v
    }

    override fun onResume() {
        super.onResume()

        layoutCalendar(
            view,
            DataManager.instance.headache,
            arguments.getInt(getString(R.string.month)),
            arguments.getInt(getString(R.string.year))
        )
    }

    private fun layoutCalendar(v: View, headache: Headache, month: Int, year: Int): View {

        this.month = month
        this.year = year

        val startDate = LocalDate(year, month, 1)
        val today = LocalDate.now()
        val previousMonth = startDate.minusMonths(1)
        val nextMonth = startDate.plusMonths(1)

        val monthTextView = view.findViewById(R.id.fragment_calendar_month_textview) as TextView
        monthTextView.text = startDate.toString("MMMM")

        val yearTextView = v.findViewById(R.id.fragment_calendar_year_textview) as TextView
        yearTextView.text = year.toString()

        val prevMonthButton = v.findViewById(R.id.fragment_calendar_previous_month_button) as Button
        val nextMonthButton = v.findViewById(R.id.fragment_calendar_next_month_button) as Button

        prevMonthButton.setOnClickListener {
            layoutCalendar(v, headache, previousMonth.monthOfYear, previousMonth.year)
        }

        nextMonthButton.setOnClickListener {
            layoutCalendar(v, headache, nextMonth.monthOfYear, nextMonth.year)
        }

        val monthCellOffset = if(startDate.dayOfWeek == 7) 0 else startDate.dayOfWeek

        val indicies = (1..6).flatMap { i -> (1..7).map { j -> Pair(i, j) } }
        for((i, j) in indicies) {
            val viewId = "calendar_week" + i + "_day" + j
            val dayView = v.findViewById(resources.getIdentifier(viewId, "id", activity.packageName)) as CalendarDayView

            val dayValue = (i-1)*7 + j - monthCellOffset
            if(dayValue >= 1 && dayValue <= LocalDate(this.year, this.month, 1).dayOfMonth().maximumValue) {

                val currentDate = LocalDate(this.year, this.month, dayValue)
                val dayDiff = Duration(
                        currentDate.toDateTimeAtStartOfDay(),
                        today.toDateTimeAtStartOfDay()
                ).standardDays

                dayView.date = currentDate
                val data = headache.getDate(currentDate)
                dayView.setHeadacheData(data)

                dayView.setOnClickListener(if(dayDiff >= 0) {
                  View.OnClickListener { v ->
                      this.daySelectedListener.onDaySelected((v as CalendarDayView).date!!)
                  }
                } else {
                  null
                })
            } else {
                dayView.date = null
                dayView.setHeadacheData(null)
                dayView.setOnClickListener(null)
            }
        }

        return v
    }

    interface IDaySelectedListener {
        fun onDaySelected(date: LocalDate)
    }
}
