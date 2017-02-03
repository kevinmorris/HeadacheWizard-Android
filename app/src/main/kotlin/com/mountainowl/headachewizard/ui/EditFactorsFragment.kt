package com.mountainowl.headachewizard.ui

import android.app.AlertDialog
import android.app.ListFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.mountainowl.headachewizard.R
import com.mountainowl.headachewizard.model.DataManager
import com.mountainowl.headachewizard.model.Factor

class EditFactorsFragment : ListFragment(), IAddFactorDialogListener {

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

        val addFactorButton = view.findViewById(R.id.fragment_edit_factors_add_new_button) as Button

        addFactorButton.setOnClickListener {
            val dialog = AddFactorDialogFragment(this)

            val ft = activity.fragmentManager
            dialog.show(ft, "dialog")
        }

        return view
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

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {

    }

    override fun factorNameEntered(factorName: String) {
        if (dataManager.factorExists(factorName)) {
            displayDuplicateFactorDialog(factorName)
        } else {
            factors = dataManager.addFactor(factorName)
            adapter.notifyDataSetChanged()
        }
    }

    private inner class EditFactorsAdapter(factors: List<Factor>) : ArrayAdapter<Factor>(activity, android.R.layout.simple_list_item_1, factors) {

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            val newView = view ?: activity.layoutInflater.inflate(R.layout.list_item_edit_factor, null)

            val factorNameField = newView.findViewById(R.id.factor_name) as TextView
            factorNameField.text = getItem(position).name

            return newView
        }
    }
}
