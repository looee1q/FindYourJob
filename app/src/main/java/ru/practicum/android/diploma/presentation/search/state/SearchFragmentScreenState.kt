package ru.practicum.android.diploma.presentation.search.state

import ru.practicum.android.diploma.domain.models.Vacancies

sealed interface SearchFragmentScreenState {

    data object Loading : SearchFragmentScreenState

    data class Content(val content: Vacancies) : SearchFragmentScreenState

    data object Error : SearchFragmentScreenState

    data object Empty : SearchFragmentScreenState

    data object Start : SearchFragmentScreenState

    data object NoInternet : SearchFragmentScreenState
}
