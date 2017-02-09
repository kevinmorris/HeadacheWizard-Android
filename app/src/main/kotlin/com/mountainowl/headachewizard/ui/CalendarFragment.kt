package com.mountainowl.headachewizard.ui

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import com.mountainowl.headachewizard.R
import com.mountainowl.headachewizard.model.DataManager
import com.mountainowl.headachewizard.model.Headache
import org.joda.time.Duration
import org.joda.time.LocalDate

class CalendarFragment : Fragment(), CalendarLayout.ICalendarLayoutListener {

    private lateinit var daySelectedListener: IDaySelectedListener

    private var month: Int = 0
    private var year: Int = 0
    private var currentYear = LocalDate.now().year

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        this.daySelectedListener = activity as IDaySelectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.month = arguments.getInt(getString(R.string.month));
        this.year = arguments.getInt(getString(R.string.year))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_calendar, container, false)

        val layout = v.findViewById(R.id.calendar) as CalendarLayout
        layout.layoutCalendarCallback = this

        val monthSpinner = v.findViewById(R.id.fragment_calendar_month_spinner) as Spinner
        val monthAdapter = ArrayAdapter.createFromResource(activity, R.array.months_array, R.layout.spinner_layout_item)
        monthAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        monthSpinner.adapter = monthAdapter
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                throw UnsupportedOperationException("not implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                layoutCalendar(position + 1, year)
            }
        }

        val yearSpinner = v.findViewById(R.id.fragment_calendar_year_spinner) as Spinner
        val yearAdapter = ArrayAdapter(
                activity,
                R.layout.spinner_layout_item,
                IntProgression.fromClosedRange(currentYear, currentYear - 100, -1).toList()
        )

        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        yearSpinner.adapter = yearAdapter
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                throw UnsupportedOperationException("not implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                layoutCalendar(month, currentYear - position)
            }

        }

        return v
    }

    override fun onResume() {
        super.onResume()

        layoutCalendar(
            view,
            DataManager.instance.headache,
            this.month,
            this.year
        )
    }

    private fun layoutCalendar(v: View, headache: Headache, month: Int, year: Int): View {

        this.month = month
        this.year = year

        val layout = v.findViewById(R.id.calendar) as CalendarLayout
        val monthSpinner = v.findViewById(R.id.fragment_calendar_month_spinner) as Spinner
        monthSpinner.setSelection(this.month - 1)

        val yearSpinner = v.findViewById(R.id.fragment_calendar_year_spinner) as Spinner
        yearSpinner.setSelection(this.currentYear - this.year)

        layout.month = this.month
        layout.year = this.year

        val startDate = LocalDate(year, month, 1)
        val today = LocalDate.now()
        val previousMonth = startDate.minusMonths(1)
        val nextMonth = startDate.plusMonths(1)

        val prevMonthButton = v.findViewById(R.id.fragment_calendar_previous_month_button) as ImageButton
        val nextMonthButton = v.findViewById(R.id.fragment_calendar_next_month_button) as ImageButton

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

                dayView.setOnClickListener(
                    if(dayDiff >= 0) {
                        View.OnClickListener { v ->
                            this.daySelectedListener.onDaySelected((v as CalendarDayView).date!!)
                        }
                    } else {
                        null
                    }
                )
            } else {
                dayView.date = null
                dayView.setHeadacheData(null)
                dayView.setOnClickListener(null)
            }
        }

        return v
    }

    override fun layoutCalendar(month: Int, year: Int) {
        layoutCalendar(view, DataManager.instance.headache, month, year)
    }
}
