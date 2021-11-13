package com.example.shoppingapptesting

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val THEME_EXTRAS_BUNDLE_KEY =
    "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY"

/**Fragment Hilt Container
* 因為Hilt會有一個 @AndroidEntryPoint 標記，所以要建立另一外一個AndroidEntryPoint來測試
* 首先先建立一個AndroidEntryPoint -> HiltTestActivity
* 在launchFragmentInHiltContainer有三個參數
* fragmentArgs : 是fragment建立時候接收的外部參數
* themeResId : fragment的layout，在這邊用系統預設的樣板
* fragmentFactory: FragmentFactory 負責在 Activity 和 parent Fragment 初始化 Fragment，所以應該在建立 Fragment 之前設定它。
* */

@ExperimentalCoroutinesApi
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    themeResId: Int = R.style.FragmentScenarioEmptyFragmentActivityTheme,
    fragmentFactory: FragmentFactory? = null,
    crossinline action: T.() -> Unit = {}
) {
    /**創建一個MainActivity的Intent 並將EmptyFragmentActivity的樣式與key附上去Intent*/
    val mainActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(THEME_EXTRAS_BUNDLE_KEY, themeResId)

    /**啟動ActivityScenario*/
    ActivityScenario.launch<HiltTestActivity>(mainActivityIntent).onActivity { hiltTestActivity->
        /**設置fragmentFactory 如果fragmentFactory不是null就設置hiltTestActivity的fragmentFactory*/
        fragmentFactory?.let {
            hiltTestActivity.supportFragmentManager.fragmentFactory = it
        }

        /**創建fragment物件，他是透過hiltTestActivity的fragmentFactory創建*/
        val fragment = hiltTestActivity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )

        /**給fragment添加外部傳入的參數*/
        fragment.arguments = fragmentArgs

        /**hiltTestActivity轉換頁面*/
        hiltTestActivity.supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        /**頁面轉換的動作開始*/
        (fragment as T).action()
    }
}