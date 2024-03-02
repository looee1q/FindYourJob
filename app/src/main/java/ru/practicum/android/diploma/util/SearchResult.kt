package ru.practicum.android.diploma.util

sealed interface SearchResult<T> {
    data class Success<T>(val data: T) : SearchResult<T>
    class Error<T> : SearchResult<T>
    class NoInternet<T> : SearchResult<T>
}
