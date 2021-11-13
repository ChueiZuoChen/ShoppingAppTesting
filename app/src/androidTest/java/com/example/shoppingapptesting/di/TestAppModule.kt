package com.example.shoppingapptesting.di

import android.content.Context
import androidx.room.Room
import com.example.shoppingapptesting.data.local.ShoppingItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    /**
     * 1. DAO測試時我們使用Room的inMemoryDatabaseBuilder()資料庫功能，
     * 這樣可以跟App原本的資料分開，不用怕測試影響到原資料，之後也不用清除測試資料。
     * 2. 透過applicationProvider可以得到context
     * */

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, ShoppingItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}