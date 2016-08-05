package com.mountainowl.headachewizard.ui

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.mountainowl.headachewizard.R
import com.mountainowl.headachewizard.model.DataManager
import com.mountainowl.headachewizard.model.Headache
import org.joda.time.Days
import org.joda.time.LocalDate

class CalendarFragment : Fragment() {

    private var daySelectedListener: IDaySelectedListener? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        this.daySelectedListener = activity as IDaySelectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        val v = inflater.inflate(R.layout.fragment_calendar, container, false)

        val args = arguments
        val date = LocalDate(0).plusDays(args.getInt(getString(R.string.days_since_0)))

        val month = date.monthOfYear
        val year = date.year

        var week = 1

        var currentDate = LocalDate(date.year, date.monthOfYear, 1)

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
                val dayView = v as CalendarDayView
                this@CalendarFragment.daySelectedListener!!.onDaySelected(dayView.date)
            }
        }

        return v
    }

    override fun onResume() {
        super.onResume()

        fillCalendar(view as ViewGroup, DataManager.getInstance(activity.applicationContext).headache)
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
