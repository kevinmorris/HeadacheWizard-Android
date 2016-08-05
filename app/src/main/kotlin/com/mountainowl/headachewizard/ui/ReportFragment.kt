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
import com.mountainowl.headachewizard.ui.components.CorrelationView

class ReportFragment : ListFragment() {

    private var dataManager: DataManager? = null
    private var factors: List<Factor>? = null
    private var adapter: ReportAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataManager = DataManager.getInstance(activity.applicationContext)
        factors = dataManager!!.getFactors()
        adapter = ReportAdapter(factors)

        listAdapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)
        return view
    }

    internal inner class ReportAdapter(factors: List<Factor>) : ArrayAdapter<Factor>(activity, android.R.layout.simple_list_item_1, factors) {

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            var view = view

            if (view == null) {
                view = activity.layoutInflater.inflate(R.layout.list_item_factor_report, null)
            }

            val factor = getItem(position)

            val factorNameField = view!!.findViewById(R.id.factor_name) as TextView
            factorNameField.text = factor.name

            val correlationView = view.findViewById(R.id.correlation_view) as CorrelationView
            correlationView.value = factor.r

            return view
        }
    }

}
