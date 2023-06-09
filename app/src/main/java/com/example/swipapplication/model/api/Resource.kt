package com.example.swipapplication.model.api

sealed class Resource<T>(
    val status: ResourceStatus,
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(ResourceStatus.SUCCESS, data)
    class Loading<T>(data: T? = null) : Resource<T>(ResourceStatus.LOADING, data)
    class Error<T>(data: T? = null, throwable: Throwable) :
        Resource<T>(ResourceStatus.ERROR, data, throwable)
}
