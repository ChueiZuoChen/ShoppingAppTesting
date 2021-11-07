package com.example.shoppingapptesting.repositories

import androidx.lifecycle.LiveData
import com.example.shoppingapptesting.data.local.ShoppingItem
import com.example.shoppingapptesting.data.remote.responses.ImageResponse
import com.example.shoppingapptesting.other.NetworkResult
import retrofit2.Response


/**
 * Why create the repository as interface?
 * Because I can use this interface to implements DefaultRepository and FakeRepository
 * DefaultRepository is for real application use,
 * FakeRepository is for write the androidTesting
 *
 * !!For testing we have to write ViewModel testcase,
 * and allows ViewModel can take two types of DefaultRepository and FakeRepository as argument
 * Because the Default and Fake they both implements ShoppingRepository interface
 * */
interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeShoppingItems():LiveData<List<ShoppingItem>>

    fun observeTotalPrice():LiveData<Float>

    suspend fun searchForImage(imageQuery:String): NetworkResult<ImageResponse>
}