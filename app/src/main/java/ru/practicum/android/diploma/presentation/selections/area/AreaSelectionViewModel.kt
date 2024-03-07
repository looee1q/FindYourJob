package ru.practicum.android.diploma.presentation.selections.area

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.models.Country

class AreaSelectionViewModel(private val filterSearchInteractor: FilterSearchInteractor) : ViewModel() {

    fun getCountryFromSharedPref(): Country? {
        return filterSearchInteractor.getCountry()
    }

    fun saveCountryToSharedPref(countryId: String?, countryName: String?) {
        if (countryName != null && countryId != null) {
            val country = Country(countryId, countryName)
            filterSearchInteractor.saveCountry(country)
        }
    fun getSelectedCountry(): LiveData<Country> = selectedCountry

    fun getCountry() {
        val filterParameters = filterSearchInteractor.getFilterParameters()
        filterParameters?.let {
            selectedCountry.value = Country(id = it.idCountry, name = it.nameCountry)
        }
    }
}
