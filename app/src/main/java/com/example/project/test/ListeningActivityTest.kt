package com.example.project.test

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.senkazakh.R
import com.example.project.activity.ListeningActivity
import org.junit.Before
import org.junit.Test

class ListeningActivityTest {

    @Before
    fun setUp() {
        ActivityScenario.launch(ListeningActivity::class.java)
    }

    @Test
    fun testAddListeningButton() {
        onView(withId(R.id.btnAddListening)).perform(click())

        onView(withId(R.id.tvListeningName))
            .check(matches(withText("Add Listening")))
    }
}
