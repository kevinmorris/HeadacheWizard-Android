package com.mountainowl.headachewizard.ui

import android.app.AlertDialog
import android.app.Dialog
import android.app.ListFragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.mountainowl.headachewizard.BuildConfig
import com.mountainowl.headachewizard.INTRO_INSTRUCTION_DIALOG_PREFS_KEY
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
            if(factors.size < BuildConfig.factorLimit) {
                val dialog = AddFactorDialogFragment(this)

                val ft = activity.fragmentManager
                dialog.show(ft, "dialog")
            } else {
                createUpgradeDialog().show()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        val prefs = activity.getPreferences(Context.MODE_PRIVATE)
        if(!prefs.contains(INTRO_INSTRUCTION_DIALOG_PREFS_KEY)) {
            val dialog = createInstructionDialog()
            dialog.show()

            val prefsEditor = prefs.edit()
            prefsEditor.putString(INTRO_INSTRUCTION_DIALOG_PREFS_KEY, INTRO_INSTRUCTION_DIALOG_PREFS_KEY)
            prefsEditor.apply()
        }
    }

    private fun createInstructionDialog() : Dialog {

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle("Welcome!")
                .setView(R.layout.dialog_initial_instructions)
                .setPositiveButton("OK") { dialog, which -> }

        val dialog = dialogBuilder.create()
        return dialog
    }

    private fun createUpgradeDialog() : Dialog {

        val view = activity.layoutInflater.inflate(R.layout.dialog_upgrade, null, false)

        val textView = view.findViewById(R.id.dialog_upgrade_factor_limit_text) as TextView
        textView.setText("You've reached your limit of ${BuildConfig.factorLimit} factors on the free app.  The full app allows an unlimited number of factors")

        val logo = view.findViewById(R.id.dialog_upgrade_full_app_logo)
        val fullAppButton = view.findViewById(R.id.dialog_upgrade_full_app_button)

        val listener = View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=com.mountainowl.headachewizard.full")
            startActivity(intent)
        }

        logo.setOnClickListener(listener)
        fullAppButton.setOnClickListener(listener)

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle("Factor Limit Reached")
                .setView(view)
                .setPositiveButton("No thanks") { dialog, which -> }

        val dialog = dialogBuilder.create()
        return dialog
    }

    private fun displayDuplicateFactorDialog(duplicateFactorName: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(R.string.app_name)
                .setMessage("You are already tracking a factor named $duplicateFactorName.  Please choose a different name.")
                .setPositiveButton("OK, got it") { dialog, which -> }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun displayDeleteFactorConfirmationDialog(deleteFactorName: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(R.string.app_name).setMessage("Are you sure you want to delete the factor named " + deleteFactorName + "?  " +
                "This will permanently remove ALL data associated with this factor from the app.")
                .setNegativeButton("No") { dialog, which -> }
                .setPositiveButton("Yes") { dialog, which ->
                    factors = dataManager.deleteFactor(deleteFactorName)
                    adapter.notifyDataSetChanged()
                }

        val dialog = dialogBuilder.create()
        dialog.show()
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

            newView.setBackgroundColor(if(position % 2 == 1) {
                resources.getColor(R.color.alternate_row_background, null)
            } else {
                resources.getColor(R.color.app_background, null)
            })

            val deleteButton = newView.findViewById(R.id.factor_delete_button)
            deleteButton.setOnClickListener {
                displayDeleteFactorConfirmationDialog(getItem(position).name)
            }

            return newView
        }
    }
}
