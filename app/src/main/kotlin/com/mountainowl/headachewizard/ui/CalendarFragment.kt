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
import org.joda.time.Days
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

        var currentDate = LocalDate(year, month, 1)
        val today = LocalDate.now()
        val previousMonth = currentDate.minusMonths(1)
        val nextMonth = currentDate.plusMonths(1)

        val monthTextView = view.findViewById(R.id.fragment_calendar_month_textview) as TextView
        monthTextView.text = currentDate.toString("MMMM")

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

        val dayOfWeek = currentDate.dayOfWeek
        var writeDays = false

        for(i in 1..5) {
            for(j in intArrayOf(7, 1, 2, 3, 4, 5, 6)) {

                if(!writeDays && j == dayOfWeek) {
                    writeDays = true
                }

                if(currentDate.monthOfYear != month) {
                    writeDays = false
                }

                val viewId = "calendar_week" + i + "_day" + j
                val dayView = v.findViewById(resources.getIdentifier(viewId, "id", activity.packageName)) as CalendarDayView

                if(writeDays) {
                    val dayDiff = Duration(
                            currentDate.toDateTimeAtStartOfDay(),
                            today.toDateTimeAtStartOfDay()
                    ).standardDays

                    dayView.date = currentDate
                    val data = headache.getDate(currentDate)
                    dayView.setHeadacheData(data)

                    currentDate = currentDate.plus(Days.days(1))

                    if(dayDiff >= 0) {
                        dayView.setOnClickListener { v ->
                            this.daySelectedListener.onDaySelected((v as CalendarDayView).date!!)
                        }
                    } else {
                        dayView.setOnClickListener(null)
                    }
                } else {
                    dayView.date = null
                    dayView.setHeadacheData(null)
                    dayView.setOnClickListener(null)
                }
            }
        }

        return v
    }

    interface IDaySelectedListener {
        fun onDaySelected(date: LocalDate)
    }
}
