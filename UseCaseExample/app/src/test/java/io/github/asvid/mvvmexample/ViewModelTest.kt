package io.github.asvid.mvvmexample

import androidx.lifecycle.SavedStateHandle
import io.github.asvid.mvvmexample.presentation.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ViewModelTest {

    private lateinit var victim: ViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        // create instance here, otherwise coroutines dispatcher won't work correctly
        victim = ViewModel(SavedStateHandle())
    }

    @Test
    fun `some test`() = runTest {
        val NEW_INPUT = "new input"

        victim.inputChanged(NEW_INPUT)
        advanceUntilIdle()
        val result = victim.state.value.currentInput

        assertEquals(NEW_INPUT, result)
    }

}