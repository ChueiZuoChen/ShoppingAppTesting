package com.example.shoppingapptesting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapptesting.data.local.ShoppingItem
import com.example.shoppingapptesting.data.remote.responses.ImageResponse
import com.example.shoppingapptesting.other.Constants
import com.example.shoppingapptesting.other.Event
import com.example.shoppingapptesting.other.Resource
import com.example.shoppingapptesting.repositories.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

/**
 * Because FakeRepository and DefaultRepository both are implement the ShoppingRepository interface,
 * Any types of ShoppingRepository class or interface all can assign in to ShoppingViewModel
 * However, we both implemented interface methods of ShoppingRepository interface on each class
 * which is DefaultRepository and FakeRepository class, then after I assign the FakeRepository and DefaultRepository class
 * I don't need to implement interface methods.
 * The advantage of this way is good structure to testing and running program,
 * I don't need to create another ViewModel for testing source from repository.
 *
 * For testing -> When we write the testcase I can assign FakeRepository in to test the program.
 * For real application running -> When we run the actually main program, I can assign DefaultRepository into.
 * */

//On view model constructor injection I need to annotate @HiltViewModel before the view model class
@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = shoppingRepository.observeShoppingItems()
    val shoppingPrice = shoppingRepository.observeTotalPrice()

    /** get image response and wrap ImageResponse data model to livedata become observable object */
    private val _image = MutableLiveData<Event<Resource<ImageResponse>>>()
    val image: LiveData<Event<Resource<ImageResponse>>> = _image

    /** I need currentImageUrl, when the image was clicked then hold to current image url to do operation */
    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl: LiveData<String> = _currentImageUrl

    /** to valid the input from edittext insertion, hold the insert item that can be observe */
    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus


    fun setCurrentImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        shoppingRepository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDB(shoppingItem: ShoppingItem) = viewModelScope.launch {
        shoppingRepository.insertShoppingItem(shoppingItem)
    }

    /** before insert shopping item,I need to valid to input is correct */
    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(
                Event(Resource.onError("The field must not be empty", null))
            )
            return
        }

        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.onError(
                        "The name of the item must not exceed" +
                                " ${Constants.MAX_NAME_LENGTH} characters", null
                    )
                )
            )
            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.onError(
                        "The price of the item must not exceed" +
                                " ${Constants.MAX_PRICE_LENGTH} characters", null
                    )
                )
            )
            return
        }
        val amount = try {
            amountString.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.onError("The amount not valid", null)
                )
            )
            return
        }

        /** it's completed validation above function*/
        val shoppingItem =
            ShoppingItem(1, name, amount, priceString.toFloat(), _currentImageUrl.value ?: "")
        insertShoppingItemIntoDB(shoppingItem)
        setCurrentImageUrl("")
        /** set status as success to notify snack bar*/
        _insertShoppingItemStatus.postValue(Event(Resource.onSuccess(shoppingItem)))
    }

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) {
            return
        }
        _image.value = Event(Resource.onLoading(null))
        viewModelScope.launch {
            val response = shoppingRepository.searchForImage(imageQuery)
            _image.value = Event(response)
        }
    }

}
























