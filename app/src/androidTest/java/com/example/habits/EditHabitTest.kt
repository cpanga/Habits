package com.example.habits

import androidx.appcompat.widget.AppCompatEditText
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.habits.database.Habit
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import java.util.*

@RunWith(AndroidJUnit4::class)
class EditHabitTest {

    @Test
    fun clickOnDays() {

        val fragArgs = bundleOf(
            "habit" to Habit(
                0,
                "Habit1",
                "Habit Desc",
                "1101101",
                9,
                11,
                5
            )
        )

        launchFragmentInContainer<CreateHabitFragment>(
            fragmentArgs = fragArgs,
            themeResId = R.style.Theme_Habits
        )

        println("Perform clicks")

        onView(withId(R.id.mon)).perform(click())
        onView(withId(R.id.wed)).perform(click())
        onView(withId(R.id.thu)).perform(click())
        onView(withId(R.id.sat)).perform(click())

        println("Assert the correct alpha values - 0110111")
        onView(withId(R.id.mon)).check(matches(withAlpha(0.5F)))
        onView(withId(R.id.tue)).check(matches(withAlpha(1F)))
        onView(withId(R.id.wed)).check(matches(withAlpha(1F)))
        onView(withId(R.id.thu)).check(matches(withAlpha(0.5F)))
        onView(withId(R.id.fri)).check(matches(withAlpha(1F)))
        onView(withId(R.id.sat)).check(matches(withAlpha(1F)))
        onView(withId(R.id.sun)).check(matches(withAlpha(1F)))

    }

    @Test
    fun testDefaultInflation() {

        val fragArgs = bundleOf(
            "habit" to null
        )

        launchFragmentInContainer<CreateHabitFragment>(
            fragmentArgs = fragArgs,
            themeResId = R.style.Theme_Habits
        )

        println("Assert the correct alpha values - 0110111")
        onView(withId(R.id.mon)).check(matches(withAlpha(0.5F)))
        onView(withId(R.id.tue)).check(matches(withAlpha(0.5F)))
        onView(withId(R.id.wed)).check(matches(withAlpha(0.5F)))
        onView(withId(R.id.thu)).check(matches(withAlpha(0.5F)))
        onView(withId(R.id.fri)).check(matches(withAlpha(0.5F)))
        onView(withId(R.id.sat)).check(matches(withAlpha(0.5F)))
        onView(withId(R.id.sun)).check(matches(withAlpha(0.5F)))

        onView(withId(R.id.reminder_time)).check(matches(hasDescendant(withText("9:00 AM"))))
        onView(withId(R.id.habit_name)).check(matches(hasDescendant(withText(""))))
        onView(withId(R.id.habit_desc)).check(matches(hasDescendant(withText(""))))



    }

    @Test
    fun testTimePicker() {

        val fragArgs = bundleOf(
            "habit" to null
        )

        launchFragmentInContainer<CreateHabitFragment>(
            fragmentArgs = fragArgs,
            themeResId = R.style.Base_Theme_Habits
        )

        onView(withId(R.id.reminder_time)).perform(click())

        sleep(500)

        onView(withId(com.google.android.material.R.id.material_timepicker_mode_button)).perform(click())
        onView(withId(com.google.android.material.R.id.material_hour_text_input)).perform(click())
        onView(
            allOf(
                isDisplayed(),
                withClassName(`is`(TextInputEditText::class.java.name))
            )
        ).perform(replaceText("11"))
        onView(withId(com.google.android.material.R.id.material_minute_text_input)).perform(click())
        onView(
            allOf(
                isDisplayed(),
                withClassName(`is`(TextInputEditText::class.java.name))
            )
        ).perform(replaceText("59"))

        onView(withText("OK")).perform(click())

        sleep(500)

        onView(withId(R.id.reminder_time)).check(matches(hasDescendant(withText("11:59 AM"))))

    }
}