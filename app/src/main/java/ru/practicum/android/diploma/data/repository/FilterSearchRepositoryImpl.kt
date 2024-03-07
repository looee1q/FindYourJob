package ru.practicum.android.diploma.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.converters.Converter
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.RequestAreasSearch
import ru.practicum.android.diploma.data.dto.ResponseAreasDto
import ru.practicum.android.diploma.data.dto.ResponseCountriesDto
import ru.practicum.android.diploma.data.dto.ResponseIndustriesDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.FilterSearchRepository
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.SearchResult

class FilterSearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : FilterSearchRepository {

    override fun getIndustries(): Flow<SearchResult<List<Industry>>> = flow {
        val response = networkClient.doRequestGetIndustries()
        when (response.resultCode) {
            SUCCESS_RESPONSE -> {
                emit(
                    SearchResult.Success(
                        convertIndustryListDtoToIndustryList((response as ResponseIndustriesDto).industries)
                    )
                )
            }

            NO_INTERNET_CONNECTION -> {
                emit(SearchResult.NoInternet())
            }

            else -> {
                emit(SearchResult.Error())
            }
        }
    }

    override fun getCountries(): Flow<SearchResult<List<Country>>> = flow {
        val response = networkClient.doRequestGetCountries()
        when (response.resultCode) {
            SUCCESS_RESPONSE -> {
                emit(
                    SearchResult.Success(
                        (response as ResponseCountriesDto)
                            .countries
                            .map {
                                Country(
                                    id = it.id,
                                    name = it.name
                                )
                            }
                    )
                )
            }

            NO_INTERNET_CONNECTION -> {
                emit(SearchResult.NoInternet())
            }

            else -> {
                emit(SearchResult.Error())
            }
        }
    }

    override fun getRegions(): Flow<SearchResult<List<Region>>> {
        return getRegionsOfCountry(null)
    }

    override fun getRegionsOfCountry(countryId: String?): Flow<SearchResult<List<Region>>> = flow {
        val response = if (countryId.isNullOrEmpty()) {
            networkClient.doRequestGetAreas()
        } else {
            val request = RequestAreasSearch(id = countryId)
            networkClient.doRequestGetAreas(request)
        }

        when (response.resultCode) {
            SUCCESS_RESPONSE -> {
                val areasDto = (response as ResponseAreasDto).areas
                val regions = Converter.fromListOfAreaDTOToListOfRegion(areasDto)
                emit(
                    SearchResult.Success(regions)
                )
            }

            NO_INTERNET_CONNECTION -> {
                emit(SearchResult.NoInternet())
            }

            else -> {
                emit(SearchResult.Error())
            }
        }
    }

    override fun saveCountry(country: Country) {
        sharedPreferences.edit().putString(COUNTRY_KEY, createJsonFromCountry(country)).apply()
    }

    override fun getCountry(): Country? {
        return createCountryFromJson(sharedPreferences.getString(COUNTRY_KEY, "") ?: "")
    }

    override fun deleteCountry() {
        sharedPreferences.edit().remove(COUNTRY_KEY).apply()
    }

    private fun convertIndustryListDtoToIndustryList(industryListDto: List<IndustryDto>): List<Industry> {
        val result = ArrayList<Industry>()
        industryListDto.forEach {
            result.add(Converter.fromIndustryDtoToIndustry(it))
            if (!it.industries.isNullOrEmpty()) {
                result.addAll(it.industries.map { industryDto ->
                    Converter.fromIndustryDtoToIndustry(industryDto)
                })
            }
        }
        return result
    }

    private fun createJsonFromCountry(country: Country): String {
        return gson.toJson(country)
    }

    private fun createCountryFromJson(json: String): Country? {
        return gson.fromJson(json, Country::class.java)
    }

    companion object {
        private const val SUCCESS_RESPONSE = 200
        private const val NO_INTERNET_CONNECTION = -1
        private const val COUNTRY_KEY = "COUNTRY"
    }
}
