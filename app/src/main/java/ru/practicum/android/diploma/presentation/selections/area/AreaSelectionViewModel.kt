package ru.practicum.android.diploma.presentation.selections.area

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.models.Country

class AreaSelectionViewModel(private val filterSearchInteractor: FilterSearchInteractor) : ViewModel() {

    private val selectedCountry = MutableLiveData<Country>()

    fun getSelectedCountry(): LiveData<Country> = selectedCountry

    fun getCountry() {
        val filterParameters = filterSearchInteractor.getFilterParameters()
        filterParameters?.let {
            selectedCountry.value = Country(id = it.idCountry, name = it.nameCountry)
        }
    }
}
