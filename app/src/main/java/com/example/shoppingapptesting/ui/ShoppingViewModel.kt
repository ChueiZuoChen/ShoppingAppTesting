package com.example.shoppingapptesting.ui

import androidx.lifecycle.ViewModel
import com.example.shoppingapptesting.repositories.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

}