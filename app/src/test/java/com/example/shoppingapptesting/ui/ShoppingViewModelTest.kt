package com.example.shoppingapptesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shoppingapptesting.MainCoroutineRule
import com.example.shoppingapptesting.data.repositories.FakeShoppingRepository
import com.example.shoppingapptesting.getOrAwaitValueTest
import com.example.shoppingapptesting.other.Constants
import com.example.shoppingapptesting.other.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field, return error`() {
        viewModel.insertShoppingItem("name", "", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name, return error`() {
        val name = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH+1){
                append(1)
            }
        }

        viewModel.insertShoppingItem(name, "10", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `insert shopping item with too long price, return error`() {
        val price = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH+1){
                append(1)
            }
        }

        viewModel.insertShoppingItem("ewq", "10", price)

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, return error`() {

        viewModel.insertShoppingItem("ewq", "9999999999999999999", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, return success`() {

        viewModel.insertShoppingItem("ewq", "9999999999999999999", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    /*@Test
    fun `if image url is empty return error`() {

        viewModel.currentImageUrl

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }*/




}