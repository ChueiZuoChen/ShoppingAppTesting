package com.example.shoppingapptesting.other


/**
 * handling the result from response,
 * OnSuccess: if the response has data then Use NetworkResult to wrap the response.
 * OnFailure: if the response hasn't data then also wrap response with message and data -> data can be remote error message
 * */
data class Resource<T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> onSuccess(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> onError(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> onLoading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}