package com.mountainowl.headachewizard.ui

import android.app.AlertDialog
import android.app.ListFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.mountainowl.headachewizard.R
import com.mountainowl.headachewizard.model.DataManager
import com.mountainowl.headachewizard.model.Factor

class EditFactorsFragment : ListFragment() {

    private lateinit var dataManager: DataManager

    private lateinit var factors: List<Factor>

    private lateinit var adapter: EditFactorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataManager = DataManager.instance
        factors = dataManager.getFactors()
        adapter = EditFactorsAdapter(factors)

        listAdapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_factors, container, false)

        val addFactorButton = view.findViewById(R.id.add_factor_button) as Button

        addFactorButton.setOnClickListener {
            val newFactorField = view.findViewById(R.id.new_factor_name) as TextView
            val factorName = newFactorField.text.toString()

            if (dataManager.factorExists(factorName)) {
                displayDuplicateFactorDialog(factorName)
            } else {
                factors = dataManager.addFactor(factorName)
                adapter.notifyDataSetChanged()
                newFactorField.text = ""
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listView.onItemClickListener = FactorClickListener()
    }

    private fun displayDuplicateFactorDialog(duplicateFactorName: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(R.string.app_name).setMessage("You are already tracking a factor named $duplicateFactorName.  Please choose a different name.").setPositiveButton("OK") { dialog, which -> }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun displayDeleteFactorConfirmationDialog(deleteFactorName: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(R.string.app_name).setMessage("Are you sure you want to delete the factor named " + deleteFactorName + "?  " +
                "This will permanently remove ALL data associated with this factor from the app.").setNegativeButton("No") { dialog, which -> }.setPositiveButton("Yes") { dialog, which ->
            factors = dataManager.deleteFactor(deleteFactorName)
            adapter.notifyDataSetChanged()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private inner class EditFactorsAdapter(factors: List<Factor>) : ArrayAdapter<Factor>(activity, android.R.layout.simple_list_item_1, factors) {

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            val newView = view ?: activity.layoutInflater.inflate(R.layout.list_item_edit_factor, null)

            val factorNameField = newView.findViewById(R.id.factor_name) as TextView
            factorNameField.text = getItem(position).name

            val deleteFactorButton = newView.findViewById(R.id.delete_factor_button) as Button
            deleteFactorButton.setOnClickListener {
                val factorName = getItem(position).name
                displayDeleteFactorConfirmationDialog(factorName)
            }

            return newView
        }
    }


    private inner class FactorClickListener : AdapterView.OnItemClickListener {

        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus.windowToken, 0)
        }

    }
}
