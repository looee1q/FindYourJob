package ru.practicum.android.diploma.presentation.selections.area

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Region

class AreaSelectionViewModel(private val filterSearchInteractor: FilterSearchInteractor) : ViewModel() {

    fun saveCountryToSharedPref(countryId: String, countryName: String) {
        val savedCountryParameters = FilterParameters(idCountry = countryId, nameCountry = countryName)
        savedCountryParameters.let { filterSearchInteractor.saveFilterParameters(it) }
    }

    fun saveRegionToSharedPref(regionId: String, regionName: String) {
        val savedCountryParameters = FilterParameters(idRegion = regionId, nameRegion = regionName)
        savedCountryParameters.let { filterSearchInteractor.saveFilterParameters(it) }
    }

    fun saveCountryAndRegionToSharedPref(countryId: String, countryName: String, regionId: String, regionName: String) {
        val savedCountryParameters = FilterParameters(
            idCountry = countryId,
            nameCountry = countryName,
            idRegion = regionId,
            nameRegion = regionName
        )
        savedCountryParameters.let { filterSearchInteractor.saveFilterParameters(it) }
    }

    fun getCountryFromSharedPref(): Country {
        val filterParameters = filterSearchInteractor.getFilterParameters()
        return Country(id = filterParameters?.idCountry ?: "", name = filterParameters?.nameCountry ?: "")
    }

    fun getRegionFromSharedPref(): Region {
        val filterParameters = filterSearchInteractor.getFilterParameters()
        return Region(id = filterParameters?.idRegion ?: "", name = filterParameters?.nameRegion ?: "", parentId = "")
    }

}
