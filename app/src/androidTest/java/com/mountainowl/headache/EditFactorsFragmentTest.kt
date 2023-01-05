package pro.kevinmorris.headache

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import pro.kevinmorris.headachewizard.MainActivity
import pro.kevinmorris.headachewizard.R
import pro.kevinmorris.headachewizard.model.Factor
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.containsString
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditFactorsFragmentTest {

    @Rule
    @JvmField
    val mainActivityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun selectEditFactors() {
        Espresso.onView(ViewMatchers.withId(R.id.icon_edit_factors))
                .perform(ViewActions.click())
    }

    @Test
    fun testEditFactorsSelected() {
        Espresso.onView(ViewMatchers.withId(R.id.fragment_edit_factors_title_text))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testAddFactor() {
        Espresso.onView(ViewMatchers.withId(R.id.fragment_edit_factors_add_new_button))
                .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.fragment_edit_factors_new_entry_text))
                .perform(ViewActions.typeText("AA1"))

        Espresso.onView(ViewMatchers.withText("OK"))
                .perform(ViewActions.click())

        Espresso.onData(withFactorName(containsString("AA1")))
                .check(matches(anything()))

    }

    private fun withFactorName(nameMatcher : Matcher<String>) : Matcher<Factor> {

        return object : TypeSafeMatcher<Factor>() {

            override fun matchesSafely(item: Factor?): Boolean {
                return nameMatcher.matches(item?.name)
            }

            override fun describeTo(description : Description) {
            }
        }
    }
}