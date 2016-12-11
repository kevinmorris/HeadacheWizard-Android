package com.mountainowl.headachewizard.ui

import android.app.ListFragment
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mountainowl.headachewizard.R
import com.mountainowl.headachewizard.model.DataManager
import com.mountainowl.headachewizard.model.Factor
import com.mountainowl.headachewizard.model.Headache
import com.mountainowl.headachewizard.ui.components.CorrelationView
import com.mountainowl.headachewizard.ui.components.HeadacheSwitchPanel
import com.mountainowl.headachewizard.ui.components.IThreewaySwitchListener
import com.mountainowl.headachewizard.ui.components.ThreewaySwitchPanel
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import java.text.DateFormat
import java.util.*

class EditDailyEntryFragment : ListFragment(), IThreewaySwitchListener {

    private lateinit var headache: Headache

    lateinit var date: LocalDate
        private set

    private lateinit var factors: List<Factor>
    private lateinit var factorValues: MutableMap<Factor, Double>
    private val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        headache = DataManager.instance.headache
        date = LocalDate(0, DateTimeZone.UTC).plusDays(arguments.getInt(getString(R.string.days_since_0)))
        initializeData(LocalDate(0, DateTimeZone.UTC).plusDays(arguments.getInt(getString(R.string.days_since_0))))
    }

    fun initializeData(date: LocalDate) {

        this.date = date

        factors = DataManager.instance.getFactors()
        factorValues = HashMap<Factor, Double>()
        for (factor in factors) {
            factorValues.put(factor, factor.getDate(date) ?: 0.0)
        }

        val adapter = EditDailyEntryAdapter(factors)

        listAdapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_daily_entry, container, false)

        val layout = view as EditDailyEntryFragmentLayout
        layout.fragment = this

        val headacheDayDateLabel = view.findViewById(R.id.headache_day_date_label) as TextView
        headacheDayDateLabel.text = DateFormat.getDateInstance(DateFormat.LONG).format(date.toDate())

        val headacheSwitchPanel = view.findViewById(R.id.headache_switch_panel) as HeadacheSwitchPanel
        val hValue = headache.getDate(date)
        headacheSwitchPanel.set(hValue)
        headacheSwitchPanel.addObserver(this)

        val doneButton = view.findViewById(R.id.fragment_edit_daily_entry_done_button)
        doneButton.setOnClickListener {
            activity.fragmentManager.popBackStack()
        }

        return view
    }

    override fun onSwitchChangedByUser(progress: Int, position: Int) {
    }

    override fun onSwitchTouchUp(progress: Int, position: Int) {

        val currentHValue = headache.getDate(date)
        val hValue = progress - 1.0

        if (currentHValue != hValue) {
            val dataManager = DataManager.instance

            headache.setDate(date, hValue)

            Thread(Runnable {
                dataManager.insertOrUpdateHeadacheEntry(date, hValue)

                for (factor in factors) {
                    factor.evaluateCorrelationParameters(headache)
                }

                handler.post(Runnable {
                    listView.invalidateViews()
                })
            }).start()
        }
    }

    private inner class EditDailyEntryAdapter(factors: List<Factor>) :
            ArrayAdapter<Factor>(activity, android.R.layout.simple_list_item_1, factors),
            IThreewaySwitchListener {

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {

            val newView = view ?: activity.layoutInflater.inflate(R.layout.list_item_edit_daily_entry, null)
            val factor = getItem(position)
            val fValue = factor.getDate(date)
            val factorNameField = newView.findViewById(R.id.factor_name) as TextView
            val correlationView = newView.findViewById(R.id.correlation_view) as CorrelationView
            correlationView.value = factor.r

            val factorSwitchPanel = newView.findViewById(R.id.factor_switch_panel) as ThreewaySwitchPanel

            factorSwitchPanel.addObserver(this)
            factorSwitchPanel.set(fValue)
            factorSwitchPanel.rowPosition = position

            factorNameField.text = factor.name + " " + factor.r

            return newView
        }

        override fun onSwitchChangedByUser(progress: Int, position: Int) {
        }

        override fun onSwitchTouchUp(progress: Int, position: Int) {

            val factor = getItem(position)
            val fValue = progress - 1.0
            if (factor.getDate(date) != fValue) {
                factor.setDate(date, fValue)

                Thread(Runnable {
                    DataManager.instance.insertOrUpdateFactorEntry(factor.id, date, factor.getDate(date))
                    factor.evaluateCorrelationParameters(headache)

                    handler.post(Runnable {
                        notifyDataSetChanged()
                    })
                }).start()
            }
        }
    }
}