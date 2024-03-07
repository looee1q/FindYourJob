package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.SearchResult

interface FilterSearchInteractor {

    fun getIndustries(): Flow<SearchResult<List<Industry>>>

    fun getCountries(): Flow<SearchResult<List<Country>>>

    fun getRegions(): Flow<SearchResult<List<Region>>>

    fun getParentRegions(parentAreaId: String): Flow<SearchResult<List<Region>>>

    fun saveCountry(country: Country)

    fun getCountry(): Country?

    fun deleteCountry()
}
