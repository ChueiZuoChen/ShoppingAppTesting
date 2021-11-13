package com.example.shoppingapptesting.ui

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shoppingapptesting.R
import com.example.shoppingapptesting.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShoppingFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp(){
        hiltRule.inject()
    }

    /**測試ShoppingFragment跳轉到ShoppingItemFragment*/
    @Test
    fun clickAtShoppingItemButton_navigateToAddShoppingItemFragment(){
        /**使用mock去抓NavController取得控制*/
        val navController = mock(NavController::class.java)

        /**
         * 透過先前寫的HiltExtension class 的launchFragmentInHiltContainer方法傳入ShoppingFragment並註冊到FragmentFactory
         * 在大括號 this:ShoppingFragment 代表可以操作ShoppingFragment裡面的內容
         * */
        launchFragmentInHiltContainer<ShoppingFragment> {
            /**設置Navigation 的ViewNavController 傳入兩個參數，當前要測試的fragment view和 Mockito取得控制的NavController*/
            Navigation.setViewNavController(requireView(),navController)
        }

        /**
         * 上面設置完畢後可以開始對View進行操作
         * 這邊先使用espresso的onView()方法給他要操作的物件，這邊是floatingActionButton
         * 然後是他的perform() 裡面的參數在 androidx.test.espresso.action.ViewActions裏面有很多操作，例如單點擊click(),雙點擊doubleClick()..etc
         * 最後在使用Mockito的verify()方法去驗證，裏面給他navController，然後測試跳轉頁面動作
         * */
        onView(withId(R.id.fabAddShoppingItem)).perform(click())
        verify(navController).navigate(
            ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
        )
    }


}