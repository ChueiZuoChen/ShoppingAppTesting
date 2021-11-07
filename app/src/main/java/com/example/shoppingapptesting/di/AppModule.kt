package com.example.shoppingapptesting.di

import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppingapptesting.data.local.ShoppingDao
import com.example.shoppingapptesting.data.local.ShoppingItemDatabase
import com.example.shoppingapptesting.data.remote.PixabayAPI
import com.example.shoppingapptesting.other.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * through dagger-hilt dependencies injection,
     * I can provide the database instance,dao instance and Retrofit api instance for dependencies injection
     * */

    @Provides
    @Singleton
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context,
    ): ShoppingItemDatabase = Room.databaseBuilder(
        context,
        ShoppingItemDatabase::class.java,
        Constants.DATABASES_NAME
    ).build()

    @Provides
    @Singleton
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    ): ShoppingDao = database.shoppingDao()

    @Provides
    @Singleton
    fun providePixabayApi():PixabayAPI = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()
        .create(PixabayAPI::class.java)


}













