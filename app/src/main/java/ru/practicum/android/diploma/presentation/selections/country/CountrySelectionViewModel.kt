package ru.practicum.android.diploma.presentation.selections.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.presentation.selections.country.state.CountrySelectionState

class CountrySelectionViewModel(private val filterSearchInteractor: FilterSearchInteractor) : ViewModel() {

    private val countrySelectionState = MutableLiveData<CountrySelectionState>()

    fun getCountrySelectionState(): LiveData<CountrySelectionState> = countrySelectionState

    fun getCountries() {
        viewModelScope.launch {
            filterSearchInteractor
                .getCountries()
                .collect {
                    if (it.isEmpty()) {
                        renderCountrySelectionState(CountrySelectionState.Empty)
                    } else {
                        renderCountrySelectionState(CountrySelectionState.Content(it))
                    }
                }
        }
    }

    private fun renderCountrySelectionState(state: CountrySelectionState) {
        countrySelectionState.postValue(state)
    }
}
