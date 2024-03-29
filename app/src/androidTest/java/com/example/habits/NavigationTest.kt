package com.example.habits

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Test
    fun navigate_to_create_habit_nav_component() {

        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        val habitListScenario = launchFragmentInContainer<HabitListFragment>(
            themeResId = R.style.HabitTheme
        )

        habitListScenario.onFragment {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, clickChildViewWithId(R.id.recycler_edit)
            )
        )

        assertEquals(navController.currentDestination?.id, R.id.CreateHabitFragment)
    }
}
