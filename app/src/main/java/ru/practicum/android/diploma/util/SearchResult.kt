package ru.practicum.android.diploma.util

sealed class SearchResult<T>(val data: T? = null) {
    class Success<T>(data: T) : SearchResult<T>(data)
    class Error<T>(data: T? = null) : SearchResult<T>(data)
    class NoInternet<T>(data: T? = null) : SearchResult<T>(data)
}
