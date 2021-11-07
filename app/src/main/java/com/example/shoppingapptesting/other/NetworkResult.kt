package com.example.shoppingapptesting.other


/**
 * use sealed class to handle the result from response,
 * OnSuccess: if the response has data then Use NetworkResult to wrap the response.
 * OnFailure: if the response hasn't data then also wrap response with message and data -> data can be remote error message
 * */
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class OnSuccess<T>(data: T?) : NetworkResult<T>()

    class OnFailure<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)

    class Loading<T> : NetworkResult<T>()

}