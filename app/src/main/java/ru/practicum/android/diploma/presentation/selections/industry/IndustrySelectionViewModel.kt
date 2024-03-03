package ru.practicum.android.diploma.presentation.selections.industry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor

class IndustrySelectionViewModel(private val filterSearchInteractor: FilterSearchInteractor) : ViewModel() {

    init {
        getIndustries()
    }

    private fun getIndustries() {
        viewModelScope.launch {
            filterSearchInteractor
                .getIndustries()
                .collect {
                }
        }
    }
}
