package io.github.asvid.mvpexample

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MainViewPresenterTest {

    private lateinit var victim: MainViewPresenter
    private lateinit var viewMock: Contract.ConcreteView

    @Before
    fun setup() {
        victim = MainViewPresenter()
        viewMock = mockk<Contract.ConcreteView>()

        every { viewMock.hideLoading() } returns Unit
        every { viewMock.displayLoading() } returns Unit
        every { viewMock.displayError(any()) } returns Unit
        every { viewMock.hideError() } returns Unit
        every { viewMock.displayItems(any()) } returns Unit
    }

    @Test
    fun `some test`() {
        val ITEM = 1
        victim.attachView(viewMock)
        verify { viewMock.displayItems(listOf()) }

        // remove not existing item
        victim.removeItem(ITEM)

        verify { viewMock.displayLoading() }
        // Model will return error
        verify { viewMock.displayError(any()) }
        verify { viewMock.hideLoading() }

        confirmVerified(viewMock)
    }
}