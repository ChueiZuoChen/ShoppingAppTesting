package com.example.shoppingapptesting.di

import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shoppingapptesting.R
import com.example.shoppingapptesting.data.local.ShoppingDao
import com.example.shoppingapptesting.data.local.ShoppingItemDatabase
import com.example.shoppingapptesting.data.remote.PixabayAPI
import com.example.shoppingapptesting.other.Constants
import com.example.shoppingapptesting.repositories.DefaultRepository
import com.example.shoppingapptesting.repositories.ShoppingRepository
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
     * I can provide the database instance,dao instance, Retrofit api instance, ViewModel instance for dependencies injection
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
    fun providePixabayApi(): PixabayAPI = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()
        .create(PixabayAPI::class.java)

    /**
     * in the Shopping viewmodel needs ShoppingRepository
     * also for create DefaultRepository instance, I need dao and api
     * */
    @Provides
    @Singleton
    fun provideDefaultShoppingRepository(
        dao: ShoppingDao,
        api: PixabayAPI
    ) = DefaultRepository(dao, api) as ShoppingRepository


    /**提供Glide instance並初始化預設當image = null時 顯示預設圖片*/
    @Provides
    @Singleton
    fun provideGlideInstance(
        @ApplicationContext context: Context,
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
    )
}

















