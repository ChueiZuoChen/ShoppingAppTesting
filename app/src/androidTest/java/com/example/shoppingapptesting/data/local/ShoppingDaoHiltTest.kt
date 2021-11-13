package com.example.shoppingapptesting.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.shoppingapptesting.getOrAwaitValue
import com.example.shoppingapptesting.launchFragmentInHiltContainer
import com.example.shoppingapptesting.ui.ShoppingFragment

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShoppingDaoHiltTest {

    // hilt injection rule
    @get:Rule
    var hiltRul = HiltAndroidRule(this)

    // single task rule
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var itemDatabase: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setUp() {
        hiltRul.inject()
        dao = itemDatabase.shoppingDao()
    }

    @After
    fun tearDown() {
        itemDatabase.close()
    }




    /**測試FragmentScenario和ActivityScenario是否將所有fragmentFactory與HiltTestActivity關聯再一起*/
    /*@Test
    fun testLunchFragmentHiltContainer() {
        launchFragmentInHiltContainer<ShoppingFragment> {

        }
    }*/

    /**
     * runBlockingTest是優化過後的runBlocking{} 因為他可以忽略delay()方法直接跳過執行下一步，並且有很多例如當前時間取得等等的
     * 對於測試room的插入，我們可以先插入資料后再重新讀出來去確認是否有包含這筆資料
     */
    @Test
    fun insertShoppingItemTest() = runBlockingTest {
        val shoppingItem = ShoppingItem( "banana", 30, 1.50f, "http://banana.jpg",1)
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
        val shoppingItem = ShoppingItem( "banana", 30, 1.50f, "http://banana.jpg",1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }


    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem( "banana", 30, 15.0f, "http://banana.jpg")
        val shoppingItem2 = ShoppingItem( "apple", 10, 12.5f, "http://apple.jpg")
        val shoppingItem3 = ShoppingItem( "gg", 17, 3.6f, "http://grap.jpg")

        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()
        assertThat(totalPriceSum).isEqualTo(30 * 15f + 10 * 12.5f + 17 * 3.6f)

    }

}



















