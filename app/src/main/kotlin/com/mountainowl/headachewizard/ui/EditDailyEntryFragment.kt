package com.mountainowl.headachewizard.ui

import android.app.ListFragment
import android.os.Bundle
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
import org.joda.time.LocalDate

import java.text.DateFormat
import java.util.HashMap

class EditDailyEntryFragment : ListFragment() {

    private var headache: Headache? = null
    private var date: LocalDate? = null
    private var factors: List<Factor>? = null
    private var factorValues: MutableMap<Factor, Double>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        headache = DataManager.getInstance(activity.applicationContext).headache
        date = LocalDate(0).plusDays(arguments.getInt(getString(R.string.days_since_0)))

        factors = DataManager.getInstance(activity.applicationContext).getFactors()
        factorValues = HashMap<Factor, Double>()
        for (factor in factors!!) {
            factorValues!!.put(factor, factor.getDate(date!!))
        }

        val adapter = EditDailyEntryAdapter(factors)

        listAdapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        val view = inflater.inflate(R.layout.fragment_edit_daily_entry, container, false)

        val headacheDayDateLabel = view.findViewById(R.id.headache_day_date_label) as TextView
        headacheDayDateLabel.text = DateFormat.getDateInstance(DateFormat.LONG).format(date!!.toDate())

        val headacheSwitchPanel = view.findViewById(R.id.headache_switch_panel) as HeadacheSwitchPanel
        val hValue = headache!!.getDate(date!!)
        headacheSwitchPanel.set(hValue)

        return view
    }


    override fun onPause() {
        super.onPause()

        val dataManager = DataManager.getInstance(activity.applicationContext)

        val headacheSwitchPanel = view!!.findViewById(R.id.headache_switch_panel) as ThreewaySwitchPanel
        val hValue = headacheSwitchPanel.get()

        //TODO: Does the fragment need a local copy of the headache data?
        headache!!.setDate(date!!, hValue)
        dataManager.insertOrUpdateHeadacheEntry(date!!, hValue)

        for (factor in factors!!) {
            val newFValue = factorValues!![factor]
            val oldFValue = factor.getDate(date!!)

            //The factor value will be what was set by switch or, if unchanged, what's already in the database
            val fValue = if (newFValue == null && oldFValue == null)
                0.0
            else newFValue ?: oldFValue

            factor.setDate(date!!, fValue)

            //TODO: Can this call be avoided if both the factor and the headache state have not changed?
            factor.evaluateCorrelationParameters(headache!!)

            //Only if the factor value for this day has changed do we store it in the database
            if (fValue != oldFValue) {
                dataManager.insertOrUpdateFactorEntry(factor.id, date!!, factor.getDate(date!!))
            }
        }
    }


    private inner class EditDailyEntryAdapter(factors: List<Factor>) : ArrayAdapter<Factor>(activity, android.R.layout.simple_list_item_1, factors), IThreewaySwitchListener {

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            var view = view

            if (view == null) {
                view = activity.layoutInflater.inflate(R.layout.list_item_edit_daily_entry, null)
            }

            val factor = getItem(position)
            val fValue = factor.getDate(date!!)
            val factorNameField = view!!.findViewById(R.id.factor_name) as TextView
            val correlationView = view.findViewById(R.id.correlation_view) as CorrelationView
            correlationView.value = factor.r

            val factorSwitchPanel = view.findViewById(R.id.factor_switch_panel) as ThreewaySwitchPanel

            factorSwitchPanel.addObserver(this)
            factorSwitchPanel.set(fValue)
            factorSwitchPanel.rowPosition = position

            factorNameField.text = factor.name + " " + factor.r

            return view
        }

        override fun onSwitchChangedByUser(progress: Int, position: Int) {
            val factor = getItem(position)
            val fValue = progress - 1.0
            factorValues!!.put(factor, fValue)
        }
    }
}
