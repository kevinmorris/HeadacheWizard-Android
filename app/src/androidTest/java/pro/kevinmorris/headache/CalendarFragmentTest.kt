package pro.kevinmorris.headache

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pro.kevinmorris.headachewizard.R
import pro.kevinmorris.headachewizard.ui.MainActivity

@RunWith(AndroidJUnit4::class)
class CalendarFragmentTest {

    @Rule
    @JvmField
    val mainActivityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun selectCalendar() {
        onView(withId(R.id.icon_calendar)).perform(click())
    }

    @Test
    fun testCalendarSelected() {
        onView(withId(R.id.fragment_calendar_header)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}