package com.mountainowl.headachewizard.ui

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextThemeWrapper
import com.mountainowl.headachewizard.R


class AddFactorDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(ContextThemeWrapper(activity, R.style.AlertDialogCustom))
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.fragment_dialog_add_factor, null))
                .setTitle("Enter a new factor name")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                    this.dialog.cancel()
                })

        val dialog = builder.create()
        return dialog
    }

    override fun onResume() {
        super.onResume()

        val window = dialog.window
        val layoutParams = window.attributes
        layoutParams.width = 1200
        layoutParams.height = 700
        window.attributes = layoutParams
    }
}