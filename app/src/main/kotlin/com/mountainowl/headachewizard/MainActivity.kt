package com.mountainowl.headachewizard

import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import com.mountainowl.headachewizard.ui.*
import com.mountainowl.headachewizard.util.MigrationAsyncTask
import org.joda.time.DateTimeZone
import org.joda.time.Days
import org.joda.time.LocalDate

val INTRO_INSTRUCTION_DIALOG_PREFS_KEY = "INTRO_INSTRUCTION_DIALOG_PREFS_KEY"
val EDIT_DAILY_ENTRY_INSTRUCTION_DIALOG_PREFS_KEY = "EDIT_DAILY_ENTRY_INSTRUCTION_DIALOG_PREFS_KEY"
val DATA_MIGRATION_KEY = "DATA_MIGRATION_KEY"

class MainActivity : Activity(),
        IDaySelectedListener,
        IEditFactorsScreenSelectedCallback,
        MigrationAsyncTask.IMigrationListener {

    private lateinit var progressDialog: ProgressDialog

    private lateinit var currentFragment: Fragment

    private var drawerShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this)

        val toolbar = findViewById(R.id.main_toolbar) as Toolbar
        val drawer = findViewById(R.id.drawer_container)

        setActionBar(toolbar)
        actionBar.title = ""
        toolbar.setNavigationOnClickListener {
            if(drawerShown) {
                hideDrawer(drawer, toolbar)
            } else {
                showDrawer(drawer, toolbar)
            }
        }

        val closeDrawerButton = findViewById(R.id.close_drawer_button) as Button
        closeDrawerButton.setOnClickListener {
            hideDrawer(drawer, toolbar)
        }

        val drawerLogo = findViewById(R.id.drawer_container_logo) as ImageView
        drawerLogo.setImageDrawable(if(BuildConfig.APPLICATION_ID.endsWith(".full"))
            resources.getDrawable(R.drawable.headache_wizard_logo, null) else
            resources.getDrawable(R.drawable.headache_wizard_free_logo, null))

        val drawerCalendarNav = findViewById(R.id.drawer_calendar_container)
        drawerCalendarNav.setOnClickListener {
            calendarSelected()
            hideDrawer(drawer, toolbar)
        }

        val drawerEditDailyEntryNav = findViewById(R.id.drawer_edit_daily_entry_container)
        drawerEditDailyEntryNav.setOnClickListener {
            editTodaySelected()
            hideDrawer(drawer, toolbar)
        }

        val drawerEdtFactorsNav = findViewById(R.id.drawer_edit_factors_container)
        drawerEdtFactorsNav.setOnClickListener {
            editFactorsSelected()
            hideDrawer(drawer, toolbar)
        }

        val drawerAboutNav = findViewById(R.id.drawer_about_container)
        drawerAboutNav.setOnClickListener {
            aboutSelected()
            hideDrawer(drawer, toolbar)
        }

        val drawerResetHelpDialogsNav = findViewById(R.id.drawer_reset_help_dialogs_container)
        drawerResetHelpDialogsNav.setOnClickListener {
            resetHelpDialogsSelected()
            hideDrawer(drawer, toolbar)
        }

        val drawerLicensesNav = findViewById(R.id.drawer_licenses_container)
        drawerLicensesNav.setOnClickListener {
            licensesSelected()
            hideDrawer(drawer, toolbar)
        }

        val prefs = getPreferences(Context.MODE_PRIVATE)
        if(BuildConfig.APPLICATION_ID.endsWith(".full") && !prefs.contains(DATA_MIGRATION_KEY)) {

            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.setProgressNumberFormat(null)
            progressDialog.setMessage("Transferring your headache data...")
            progressDialog.show()

            val prefsEditor = prefs.edit()

            val migrationTask = MigrationAsyncTask(this)
            migrationTask.execute()

            prefsEditor.putString(DATA_MIGRATION_KEY, DATA_MIGRATION_KEY)

            prefsEditor.apply()

        } else {
            displayContent()
        }
    }

    override fun processMigrationProgress(progress: Int) {
        progressDialog.progress = progress
    }

    override fun migrationComplete() {
        progressDialog.dismiss()
        displayContent()
    }

    private fun displayContent() {
        val fm = fragmentManager
        val displayedFragment = fm.findFragmentById(R.id.fragment_container)
        if (displayedFragment == null) {
            val args = Bundle()
            val today = LocalDate.now()
            args.putInt(getString(R.string.month), today.monthOfYear)
            args.putInt(getString(R.string.year), today.year)

            val prefs = getPreferences(Context.MODE_PRIVATE)
            currentFragment = CalendarFragment()
            currentFragment.arguments = args

            val ft = fm.beginTransaction()
            ft.replace(R.id.fragment_container, currentFragment)
            ft.commit()

            if (!prefs.contains(INTRO_INSTRUCTION_DIALOG_PREFS_KEY)) {
                editFactorsSelected()
            }

        } else {
            currentFragment = displayedFragment
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.icon_calendar -> calendarSelected()
            R.id.icon_edit_today -> editTodaySelected()
            R.id.icon_edit_factors -> editFactorsSelected()
            else -> {
            }
        }

        return true
    }

    private fun showDrawer(drawer: View, toolbar : Toolbar) {

        val width = toolbar.measuredWidth

        val layoutParams = drawer.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = width
        layoutParams.leftMargin = -width
        drawer.layoutParams = layoutParams

        drawer.visibility = VISIBLE

        drawer.animate().translationX(width + 0.0f)
        drawerShown = true
    }

    private fun hideDrawer(drawer: View, toolbar : Toolbar) {

        val width = toolbar.measuredWidth

        drawer.animate().translationX(-width + 0.0f)
        drawerShown = false
    }

    private fun aboutSelected() {
        val view = layoutInflater.inflate(R.layout.dialog_about, null, false)
        val versionNumberText = view.findViewById(R.id.dialog_about_version_number_text) as TextView
        versionNumberText.text = "Version ${BuildConfig.VERSION_CODE} ${BuildConfig.VERSION_NAME}"

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("About Headache Wizard")
                .setView(view)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })



        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun resetHelpDialogsSelected() {
        val prefs = getPreferences(Context.MODE_PRIVATE)
        val prefsEditor = prefs.edit()

        prefsEditor.remove(INTRO_INSTRUCTION_DIALOG_PREFS_KEY)
        prefsEditor.remove(EDIT_DAILY_ENTRY_INSTRUCTION_DIALOG_PREFS_KEY)
        prefsEditor.apply()

        Toast.makeText(this, R.string.toast_reset_help_dialogs, Toast.LENGTH_LONG).show()
    }

    private fun licensesSelected() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Licensing")
                .setView(R.layout.dialog_licenses)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun calendarSelected() {
        fragmentManager.popBackStack()
    }

    private fun editTodaySelected() {
        launchEditDailyEntryFragment(LocalDate(System.currentTimeMillis()))
    }

    private fun editFactorsSelected() {
        val fm = fragmentManager

        if (currentFragment !is CalendarFragment) {
            fm.popBackStack()
        }

        currentFragment = EditFactorsFragment()
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.fragment_container, currentFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun launchEditDailyEntryFragment(date: LocalDate) {
        val fm = fragmentManager

        if (currentFragment !is CalendarFragment) {
            fm.popBackStack()
        }

        val args = Bundle()
        args.putInt(getString(R.string.days_since_0), Days.daysBetween(LocalDate(0, DateTimeZone.UTC), date).days)

        currentFragment = EditDailyEntryFragment()
        currentFragment.arguments = args

        val transaction = fm.beginTransaction()
        transaction.replace(R.id.fragment_container, currentFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDaySelected(date: LocalDate) {
        launchEditDailyEntryFragment(date)
    }

    override fun editFactorsScreenSelected() {
        editFactorsSelected()
    }
}
