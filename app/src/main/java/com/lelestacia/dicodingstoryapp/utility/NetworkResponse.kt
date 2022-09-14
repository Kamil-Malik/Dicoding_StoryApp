package com.lelestacia.dicodingstoryapp.utility

sealed class NetworkResponse<out T> {
    object None : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class GenericException(val code: Int?, val cause: String?) : NetworkResponse<Nothing>()
    object NetworkException : NetworkResponse<Nothing>()
}