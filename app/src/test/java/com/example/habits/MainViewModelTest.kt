
package com.example.habits

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.habits.database.HabitDao
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class ViewModelTests {

    // TODO - doesn't work because need application passed in. Solution is to use dependency injection?

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val habitDao: HabitDao = mock(HabitDao::class.java)

    @Test
    fun quantity_twelve_cupcakes() {
        val viewModel = HabitViewModel(habitDao)
        viewModel.fabVisible.postValue(false)
        assertEquals(false, viewModel.fabVisible.value)
    }
}
