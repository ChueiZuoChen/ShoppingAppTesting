package com.example.shoppingapptesting.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppingapptesting.data.local.ShoppingItem
import com.example.shoppingapptesting.data.remote.responses.ImageResponse
import com.example.shoppingapptesting.other.NetworkResult

/**
 * [Repository testing]
 * create a fake repository to testing the repository working well
 * */

class FakeRepository : ShoppingRepository {

    private val shoppingItems = mutableListOf<ShoppingItem>()

    /**
     * fake observable livedata
     * */
    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false

    private fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun reFreshShoppingItems() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float {
        return shoppingItems.map { shoppingItem ->
            shoppingItem.price * shoppingItem.amount
        }.sum()
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        reFreshShoppingItems()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        reFreshShoppingItems()
    }

    override fun observeShoppingItems(): LiveData<List<ShoppingItem>> {
        return observableShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    override suspend fun searchForImage(imageQuery: String): NetworkResult<ImageResponse> {
        return if (shouldReturnNetworkError) {
            NetworkResult.OnFailure("Error", null)
        } else {
            NetworkResult.OnSuccess(ImageResponse(listOf(), 0, 0))
        }
    }

}