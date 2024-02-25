package ru.practicum.android.diploma.presentation.search.state

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface SearchFragmentScreenState {

    data object Loading : SearchFragmentScreenState

    data class Content(val vacancies: List<Vacancy>) : SearchFragmentScreenState

    data object Error : SearchFragmentScreenState

    data object Empty : SearchFragmentScreenState

    data object Start : SearchFragmentScreenState
}
