package com.example.habits

import android.widget.Button
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import junit.framework.TestCase.assertEquals
import org.junit.Test

class NavigationTest {

    @Test
    fun navigate_to_create_habit_nav_component() {

        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        val habitListScenario = launchFragmentInContainer<HabitListFragment>(
            themeResId = R.style.Base_Theme_Habits
        )

        habitListScenario.onFragment { fragment ->

            navController.setGraph(R.navigation.nav_graph)

            Navigation.setViewNavController(fragment.requireView(), navController)

            onView(withId(R.id.recycler_view))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click() )
                )

            // assertEquals(navController.currentDestination?.id, R.id.CreateHabitFragment)
        }
    }
}