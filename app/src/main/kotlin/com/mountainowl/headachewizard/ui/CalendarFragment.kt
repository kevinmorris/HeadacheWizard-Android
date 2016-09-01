package com.mountainowl.headachewizard.ui

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mountainowl.headachewizard.R
import com.mountainowl.headachewizard.model.DataManager
import com.mountainowl.headachewizard.model.Headache
import org.joda.time.Days
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

        val args = arguments
        this.month = args.getInt(getString(R.string.month))
        this.year = args.getInt(getString(R.string.year))

        return layoutCalendar(v, this.month, this.year)
    }

    override fun onResume() {
        super.onResume()

        fillCalendar(view as ViewGroup, DataManager.instance.headache)
    }

    private fun layoutCalendar(v: View, month: Int, year: Int): View {

        var week = 1
        var currentDate = LocalDate(year, month, 1)

        for(i in 1..5) {
            for(j in 1..7) {
                val viewId = "calendar_week" + i + "_day" + j
                val dayView = v.findViewById(resources.getIdentifier(viewId, "id", activity.packageName)) as CalendarDayView
                dayView.setOnClickListener(null)
            }
        }

        while (currentDate.monthOfYear == month) {

            val dayOfWeek = currentDate.dayOfWeek

            val viewId = "calendar_week" + week + "_day" + dayOfWeek
            val dayView = v.findViewById(resources.getIdentifier(viewId, "id", activity.packageName)) as CalendarDayView
            dayView.date = currentDate
            currentDate = currentDate.plus(Days.days(1))

            if (dayOfWeek == 6) {
                week += 1
            }

            dayView.setOnClickListener { v ->
                this.daySelectedListener.onDaySelected((v as CalendarDayView).date!!)
            }
        }

        return v
    }

    private fun fillCalendar(v: ViewGroup, headache: Headache) {

        for (i in 0..v.childCount - 1) {

            val child = v.getChildAt(i)
            if (child is CalendarDayView) {
                val date = child.date
                if (date != null) {
                    val data = headache.getDate(date)
                    child.setHeadacheData(data)
                }
            } else if (child is ViewGroup) {
                fillCalendar(child, headache)
            }
        }
    }

    interface IDaySelectedListener {
        fun onDaySelected(date: LocalDate)
    }
}
