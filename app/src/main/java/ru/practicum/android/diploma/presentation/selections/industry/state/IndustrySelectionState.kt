package ru.practicum.android.diploma.presentation.selections.industry.state

import ru.practicum.android.diploma.domain.models.Industry

sealed interface IndustrySelectionState {

    data object Error : IndustrySelectionState

    data object NoInternet : IndustrySelectionState

    data object Loading : IndustrySelectionState

    data class Content(val industries: List<Industry>) : IndustrySelectionState

    data object Empty : IndustrySelectionState

    data class ChangeCheckedIndustry(
        val industries: List<Industry>,
        val oldPos: Int,
        val newPos: Int
    ) : IndustrySelectionState
}
