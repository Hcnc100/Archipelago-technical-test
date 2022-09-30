package com.d34th.nullpointer.archipielago.core.states

sealed class Resource<out T> {
    object Init : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    object Failure : Resource<Nothing>()
}