package ru.practicum.android.diploma.ui.search

import ru.practicum.android.diploma.data.dto.Vacancy

sealed interface SearchFragmentScreenState {
    object Loading : SearchFragmentScreenState

    data class Content(val vacancy: ArrayList<Vacancy>) : SearchFragmentScreenState

    class Error : SearchFragmentScreenState

    class Empty : SearchFragmentScreenState

    class Start : SearchFragmentScreenState
}
