package com.example.shoppingapptesting.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.shoppingapptesting.getOrAwaitValue
import com.example.shoppingapptesting.ui.data.local.ShoppingDao
import com.example.shoppingapptesting.ui.data.local.ShoppingItem
import com.example.shoppingapptesting.ui.data.local.ShoppingItemDatabase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * @ExperimentalCoroutinesApi - use coroutine on AndroidTest
 * @RunWith(AndroidJUnit4::class) - use AndroidJUnit4 annotation
 * @SmallTest - normal small test, not include network testing(@MediumTest)
 * */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    //Rule cannot be private
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var itemDatabase: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setUp() {
        /**
         * 1. DAO測試時我們使用Room的inMemoryDatabaseBuilder()資料庫功能，
         * 這樣可以跟App原本的資料分開，不用怕測試影響到原資料，之後也不用清除測試資料。
         * 2. 透過applicationProvider可以得到context
         * */
        itemDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = itemDatabase.shoppingDao()
    }

    @After
    fun tearDown() {
        itemDatabase.close()
    }

    /**
     * runBlockingTest是優化過後的runBlocking{} 因為他可以忽略delay()方法直接跳過執行下一步，並且有很多例如當前時間取得等等的
     * 對於測試room的插入，我們可以先插入資料后再重新讀出來去確認是否有包含這筆資料
     */
    @Test
    fun insertShoppingItemTest() = runBlockingTest {
        val shoppingItem = ShoppingItem(2, "banana", 30, 1.50f, "http://banana.jpg")
        dao.insertShoppingItem(shoppingItem)

        /**
         * because query as livedata is Asynchronous need coroutines to get data,
         * but in here is test case we don't import all coroutines library
         * so we need use google provides extension library "LiveDataUtilAndroidTest"
         * have one function called "getOrAwaitValue()" to get our data in here
         * */
        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        //user google truth library's contains() method
        assertThat(allShoppingItems).contains(shoppingItem)
    }


    @Test
    fun deleteShoppingItemTest() = runBlockingTest {
        val shoppingItem = ShoppingItem(2, "banana", 30, 1.50f, "http://banana.jpg")
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }


    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem(1, "banana", 30, 15.0f, "http://banana.jpg")
        val shoppingItem2 = ShoppingItem(2, "apple", 10, 12.5f, "http://apple.jpg")
        val shoppingItem3 = ShoppingItem(3, "grap", 17, 3.6f, "http://grap.jpg")

        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()
        assertThat(totalPriceSum).isEqualTo(30 * 15f + 10 * 12.5f + 17 * 3.6f)

    }

}



















