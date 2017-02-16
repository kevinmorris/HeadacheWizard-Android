package com.mountainowl.headachewizard

import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.ActionBarDrawerToggle
import android.support.v4.widget.DrawerLayout
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.mountainowl.headachewizard.ui.*
import org.joda.time.DateTimeZone
import org.joda.time.Days
import org.joda.time.LocalDate

val INTRO_INSTRUCTION_DIALOG_PREFS_KEY = "INTRO_INSTRUCTION_DIALOG_PREFS_KEY"
val EDIT_DAILY_ENTRY_INSTRUCTION_DIALOG_PREFS_KEY = "EDIT_DAILY_ENTRY_INSTRUCTION_DIALOG_PREFS_KEY"

class MainActivity : Activity(), IDaySelectedListener, IEditFactorsScreenSelectedCallback {

    private lateinit var drawerListener: ActionBarDrawerToggle

    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar = actionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)

        setContentView(R.layout.activity_main)

        val drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        drawerListener = object : ActionBarDrawerToggle(this,
                drawerLayout,
                R.drawable.hamburger,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_closed) {

        }

        drawerLayout.setDrawerListener(drawerListener)

        val drawerCalendarNav = findViewById(R.id.drawer_calendar_container)
        drawerCalendarNav.setOnClickListener {
            drawerLayout.closeDrawers()
            calendarSelected()
        }

        val drawerEditDailyEntryNav = findViewById(R.id.drawer_edit_daily_entry_container)
        drawerEditDailyEntryNav.setOnClickListener {
            drawerLayout.closeDrawers()
            editTodaySelected()
        }

        val drawerEdtFactorsNav = findViewById(R.id.drawer_edit_factors_container)
        drawerEdtFactorsNav.setOnClickListener {
            drawerLayout.closeDrawers()
            editFactorsSelected()
        }

        val drawerAboutNav = findViewById(R.id.drawer_about_container)
        drawerAboutNav.setOnClickListener {
            drawerLayout.closeDrawers()
            aboutSelected()
        }

        val drawerResetHelpDialogsNav = findViewById(R.id.drawer_reset_help_dialogs_container)
        drawerResetHelpDialogsNav.setOnClickListener {
            drawerLayout.closeDrawers()
            resetHelpDialogsSelected()
        }

        val drawerLicensesNav = findViewById(R.id.drawer_licenses_container)
        drawerLicensesNav.setOnClickListener {
            drawerLayout.closeDrawers()
            licensesSelected()
        }

        val fm = fragmentManager
        val displayedFragment = fm.findFragmentById(R.id.fragment_container)
        if(displayedFragment == null) {
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

            if(!prefs.contains(INTRO_INSTRUCTION_DIALOG_PREFS_KEY)) {
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

        if (drawerListener.onOptionsItemSelected(item)) {
            return true
        }

        when (item.itemId) {
            R.id.icon_calendar -> calendarSelected()
            R.id.icon_edit_today -> editTodaySelected()
            R.id.icon_edit_factors -> editFactorsSelected()
            else -> {
            }
        }

        return true
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
