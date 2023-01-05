package pro.kevinmorris.headachewizard.ui

import android.app.ListFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import pro.kevinmorris.headachewizard.R
import pro.kevinmorris.headachewizard.model.DataManager
import pro.kevinmorris.headachewizard.model.Factor
import pro.kevinmorris.headachewizard.ui.components.CorrelationView

class ReportFragment : ListFragment() {

    private lateinit var dataManager: DataManager
    private lateinit var factors: List<Factor>
    private var adapter: ReportAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataManager = DataManager.instance
        factors = dataManager.getFactors()
        adapter = ReportAdapter(factors)

        listAdapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)
        return view
    }

    internal inner class ReportAdapter(factors: List<Factor>) : ArrayAdapter<Factor>(activity, android.R.layout.simple_list_item_1, factors) {

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            var view = view

            if (view == null) {
                view = activity.layoutInflater.inflate(R.layout.list_item_factor_report, null)
            }

            getItem(position)?.also { factor ->

                val factorNameField = view!!.findViewById(R.id.factor_name) as TextView
                factorNameField.text = factor.name

                val correlationView = view.findViewById(R.id.correlation_view) as CorrelationView
                correlationView.value = factor.r
            }

            return view!!
        }
    }

}
