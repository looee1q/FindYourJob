package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.converters.Converter
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.ResponseCountriesDto
import ru.practicum.android.diploma.data.dto.ResponseIndustriesDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.FilterSearchRepository
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industry

class FilterSearchRepositoryImpl(private val networkClient: NetworkClient) : FilterSearchRepository {

    override fun getIndustries(): Flow<List<Industry>> = flow {
        val response = networkClient.doRequestGetIndustries()
        when (response.resultCode) {
            SUCCESS_RESPONSE -> {
                emit(convertIdustryListDtoToIndustryList((response as ResponseIndustriesDto).industries))
            }

            NO_INTERNET_CONNECTION -> {
                emit(listOf())
            }

            else -> {
                emit(listOf())
            }
        }
    }

    override fun getCountries(): Flow<List<Country>> = flow {
        val response = networkClient.doRequestGetCountries()
        when (response.resultCode) {
            SUCCESS_RESPONSE -> {
                emit(
                    (response as ResponseCountriesDto)
                        .countries
                        .map {
                            Country(
                                id = it.id,
                                name = it.name,
                                url = it.url
                            )
                        }
                )
            }

            NO_INTERNET_CONNECTION -> {
                emit(listOf())
            }

            else -> {
                emit(listOf())
            }
        }
    }

    private fun convertIdustryListDtoToIndustryList(industryListDto: List<IndustryDto>): List<Industry> {
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

    companion object {
        private const val SUCCESS_RESPONSE = 200
        private const val NO_INTERNET_CONNECTION = -1
    }
}
