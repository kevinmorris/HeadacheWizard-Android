package com.mountainowl.headachewizard

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.support.v4.app.ActionBarDrawerToggle
import android.support.v4.widget.DrawerLayout
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import com.mountainowl.headachewizard.ui.CalendarFragment
import com.mountainowl.headachewizard.ui.EditDailyEntryFragment
import com.mountainowl.headachewizard.ui.EditFactorsFragment
import org.joda.time.Days
import org.joda.time.LocalDate

class MainActivity : Activity(), CalendarFragment.IDaySelectedListener {

    private var drawerLayout: DrawerLayout? = null
    private var drawerList: ListView? = null
    protected var drawerListener: ActionBarDrawerToggle? = null

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar = actionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)


        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        drawerList = findViewById(R.id.navigation_drawer) as ListView
        drawerList!!.adapter = ArrayAdapter(this,
                R.layout.navigation_drawer_item,
                resources.getStringArray(R.array.navigation_drawer_items))

        drawerListener = object : ActionBarDrawerToggle(this,
                drawerLayout,
                R.drawable.icon_navigation_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_closed) {

        }

        drawerLayout!!.setDrawerListener(drawerListener)

        val fm = fragmentManager

        val args = Bundle()
        val today = LocalDate.now()
        val todayDays = Days.daysBetween(LocalDate(0), today).days
        args.putInt(getString(R.string.month), today.monthOfYear)
        args.putInt(getString(R.string.year), today.year)

        currentFragment = CalendarFragment()
        currentFragment!!.arguments = args

        val ft = fm.beginTransaction()
        ft.replace(R.id.fragment_container, currentFragment)
        ft.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (drawerListener!!.onOptionsItemSelected(item)) {
            return true
        }

        when (item.itemId) {
            R.id.icon_calendar -> calendarSelected()
            R.id.icon_edit_today -> editTodaySelected()
            R.id.icon_edit_factors -> editFactorsSelected()
            R.id.icon_report -> reportSelected()
            else -> {
            }
        }

        return true
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

    private fun reportSelected() {

    }

    private fun launchEditDailyEntryFragment(date: LocalDate) {
        val fm = fragmentManager

        if (currentFragment !is CalendarFragment) {
            fm.popBackStack()
        }

        val args = Bundle()
        args.putInt(getString(R.string.days_since_0), Days.daysBetween(LocalDate(0), date).days)

        currentFragment = EditDailyEntryFragment()
        currentFragment!!.arguments = args

        val transaction = fm.beginTransaction()
        transaction.replace(R.id.fragment_container, currentFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDaySelected(date: LocalDate) {
        launchEditDailyEntryFragment(date)
    }
}
