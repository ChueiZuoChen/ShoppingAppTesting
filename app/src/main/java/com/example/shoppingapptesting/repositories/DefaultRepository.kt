package com.example.shoppingapptesting.repositories

import androidx.lifecycle.LiveData
import com.example.shoppingapptesting.data.local.ShoppingDao
import com.example.shoppingapptesting.data.local.ShoppingItem
import com.example.shoppingapptesting.data.remote.PixabayAPI
import com.example.shoppingapptesting.data.remote.responses.ImageResponse
import com.example.shoppingapptesting.other.NetworkResult
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject


class DefaultRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {


    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): NetworkResult<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let NetworkResult.Success(it)
                } ?: NetworkResult.Failure("An unknown occurred", null)
            } else {
                NetworkResult.Failure("An unknown occurred", null)
            }
        } catch (e: Exception) {
            NetworkResult.Failure("Cannot connection internet", null)
        }
    }


}