package com.example.tacticore_mobileapps_20262301681033 // уверете се, че пакетът съвпада с MainActivity.kt

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testHeroListClickNavigatesToDetail() {
        // Клик върху първия герой в RecyclerView (позиция 0)
        onView(withId(R.id.recyclerViewHeroes))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        // Проверка дали детайлният фрагмент се е заредил
        onView(withId(R.id.heroName)).check(matches(isDisplayed()))
    }
}
