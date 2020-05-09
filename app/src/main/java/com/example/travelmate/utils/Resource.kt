package com.example.travelmate.utils

class AppError(
    val messageRes: Int? = null,
    val message: String? = null
)

sealed class Resource<T>(
    val data: T? = null,
    val error: AppError? = null
) {

    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(error: AppError, data: T? = null) : Resource<T>(data, error)
}