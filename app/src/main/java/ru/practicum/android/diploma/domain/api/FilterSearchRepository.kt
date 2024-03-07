package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.SearchResult

interface FilterSearchRepository {

    fun getIndustries(): Flow<SearchResult<List<Industry>>>

    fun getCountries(): Flow<SearchResult<List<Country>>>

    fun getRegions(): Flow<SearchResult<List<Region>>>

    fun getRegionsOfCountry(countryId: String?): Flow<SearchResult<List<Region>>>

    fun saveCountry(country: Country)

    fun getCountry(): Country?

    fun deleteCountry()

    fun saveIndustry(industry: Industry)

    fun getIndustry(): Industry?

    fun deleteIndustry()
}
