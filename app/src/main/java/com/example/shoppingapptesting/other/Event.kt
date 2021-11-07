package com.example.shoppingapptesting.other

/**
 * if I rotate the device
 * the livedata will automatically emit again and it will again show that snake bar even though the event already passed
 * and that's why I have event class here.
 * It will check the event is already handle or not.
 * So, I need this class to handle livedata on view model.
 * */

class Event<out T>(
    private val content: T
) {
    var hasBeenHandle = false

    /**
     * Return the content and prevent its handle again
     * */
    fun getContentIfNotHandle(): T? {
        return if (hasBeenHandle) {
            null
        } else {
            hasBeenHandle = true
            content
        }
    }

    fun peekContent():T?{
        return content
    }
}