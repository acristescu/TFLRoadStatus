package io.zenandroid.greenfield.roadstatus

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.typeText
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.zenandroid.greenfield.R
import io.zenandroid.greenfield.model.Road
import io.zenandroid.greenfield.util.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.InputStreamReader

/**
 * created by acristescu
 */

@RunWith(AndroidJUnit4::class)
class RoadStatusActivityTest {

    @Rule @JvmField
    var feedActivityActivityTestRule = ActivityTestRule(RoadStatusActivity::class.java)
    private lateinit var mockResponse: ArrayList<Road>

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource)
        val gson = GsonBuilder().create()

        mockResponse = gson.fromJson(
                InputStreamReader(javaClass.classLoader!!.getResourceAsStream("mock_data.json")),
                object : TypeToken<ArrayList<Road>>() {}.type
        )
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource)
    }

    /*
    Given a valid road ID is specified
    When the client is run
    Then the road ‘displayName’ should be displayed

    Given a valid road ID is specified
    When the client is run
    Then the road ‘statusSeverity’ should be displayed as ‘Road Status’

    Given a valid road ID is specified
    When the client is run
    Then the road ‘statusSeverityDescription’ should be displayed as ‘Road Status Description’
     */
    @Test
    fun testCorrectDataIsDisplayed() {
        val roadData = mockResponse[0]
        val expectedDetails = "${roadData.displayName}\nRoad Status: ${roadData.statusSeverity}\nRoad Status Description: ${roadData.statusSeverityDescription}"

        onView(withId(R.id.roadTextEdit)).perform(typeText("A2"))
        onView(withId(R.id.searchButton)).perform(click())
        onView(withId(R.id.detailsTextView)).check(matches(withText(expectedDetails)))
    }

    /*
    Given an invalid road ID is specified
    When the client is run
    Then the application should return an informative error
     */
    @Test
    fun testErrorIsDisplayed() {
        onView(withId(R.id.roadTextEdit)).perform(typeText("ERROR_ROAD"))
        onView(withId(R.id.searchButton)).perform(click())
        onView(withId(R.id.detailsTextView)).check(matches(withText("The following road id is not recognised: ERROR_ROAD")))
    }
}
