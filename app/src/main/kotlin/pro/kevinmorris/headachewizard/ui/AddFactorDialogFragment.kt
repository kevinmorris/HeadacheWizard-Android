package pro.kevinmorris.headachewizard.ui

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import pro.kevinmorris.headachewizard.R


class AddFactorDialogFragment(val listener : IAddFactorDialogListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.fragment_dialog_add_factor, null))
                .setTitle("Enter a new factor name")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    val factorNameEditText = this.dialog.findViewById(R.id.fragment_edit_factors_new_entry_text) as EditText
                    listener.factorNameEntered(factorNameEditText.text.toString())
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                    this.dialog.cancel()
                })

        val dialog = builder.create()
        return dialog
    }
}