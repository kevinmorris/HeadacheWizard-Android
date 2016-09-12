package com.mountainowl.headachewizard.ui

import android.app.AlertDialog
import android.app.ListFragment
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
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

    private inner class EditFactorsAdapter(factors: List<Factor>) : ArrayAdapter<Factor>(activity, android.R.layout.simple_list_item_1, factors) {

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            val newView = view ?: activity.layoutInflater.inflate(R.layout.list_item_edit_factor, null)

            val factorNameField = newView.findViewById(R.id.factor_name) as TextView
            factorNameField.text = getItem(position).name

            val nameContainer = newView.findViewById(R.id.list_item_edit_factor_name_container)
            newView.setOnTouchListener(FactorTouchListener(position, newView, context))

            return newView
        }
    }

    private inner class FactorTouchListener(private val position: Int, private val view: View, context: Context) : View.OnTouchListener {

        private var xDown : Float? = 0.0f

        override fun onTouch(v: View?, event: MotionEvent?) = when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                xDown = event?.x
                true
            }
            MotionEvent.ACTION_UP -> {
                val xUp = event?.x
                val xDownLocal = xDown
                if(xDownLocal != null && xUp != null && Math.abs(xDownLocal - xUp) > 200) {
                    val deleteFactorButton = view.findViewById(R.id.list_item_edit_factor_delete) as TextView
                    val nameContainer = view.findViewById(R.id.list_item_edit_factor_name_container)

                    val animation = TranslateAnimation(
                        if ((xUp - xDownLocal) > 0) -300.0f else 0.0f,
                        if ((xUp - xDownLocal) > 0) 0.0f else -300.0f,
                        0.0f,
                        0.0f
                    )

                    animation.duration = 250
                    animation.fillAfter = true
                    nameContainer.startAnimation(animation);


                    val deleteFactorButtonClicked = View.OnClickListener {
                        val factorName = adapter.getItem(position).name
                        displayDeleteFactorConfirmationDialog(factorName)
                    }

                    deleteFactorButton.setOnClickListener(if (nameContainer.translationX < 0) deleteFactorButtonClicked else null)
                }
                false
            }
            else -> {
                true
            }
        }

        private inner class SwipeListener : GestureDetector.SimpleOnGestureListener() {

            override fun onDown(e: MotionEvent) = true

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {

                val deleteFactorButton = view.findViewById(R.id.list_item_edit_factor_delete) as TextView
                val nameContainer = view.findViewById(R.id.list_item_edit_factor_name_container)

                val xDelta = e2.x - e1.x
                val yDelta = e2.y - e1.y

                if((Math.abs(xDelta) > Math.abs(yDelta)) && (Math.abs(xDelta) > 100)) {
                    nameContainer.translationX = if (xDelta > 0) 0.0f else -300.0f

                    val deleteFactorButtonClicked = View.OnClickListener {
                        val factorName = adapter.getItem(position).name
                        displayDeleteFactorConfirmationDialog(factorName)
                    }

                    deleteFactorButton.setOnClickListener(if (nameContainer.translationX < 0) deleteFactorButtonClicked else null)
                }

                return false
            }
        }
    }


    private inner class FactorClickListener : AdapterView.OnItemClickListener {

        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus.windowToken, 0)
        }

    }
}
