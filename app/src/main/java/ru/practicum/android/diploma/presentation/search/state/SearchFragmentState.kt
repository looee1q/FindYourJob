package ru.practicum.android.diploma.presentation.search.state

import ru.practicum.android.diploma.domain.models.Vacancies

sealed interface SearchFragmentState {

    data object Loading : SearchFragmentState

    data class Content(val content: Vacancies) : SearchFragmentState

    data object Error : SearchFragmentState

    data object Empty : SearchFragmentState

    data object Start : SearchFragmentState

    data object NoInternet : SearchFragmentState
}
