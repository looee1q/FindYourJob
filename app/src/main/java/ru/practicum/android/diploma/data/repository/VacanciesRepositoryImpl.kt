package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.converters.Converter
import ru.practicum.android.diploma.data.dto.RequestSimilarVacancySearch
import ru.practicum.android.diploma.data.dto.RequestVacancySearch
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacanciesRepositoryImpl(private val networkClient: NetworkClient) : VacanciesRepository {

    override suspend fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<Vacancies> = flow {
        val requestVacanciesListSearch = Converter.fromVacanciesRequestToRequestVacanciesListSearch(vacanciesRequest)
        val response = networkClient.doRequestSearchVacancies(requestVacanciesListSearch) as ResponseVacanciesListDto
        emit(Converter.fromResponseVacanciesListDtoToVacancies(response))
    }

    override suspend fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails> = flow {
        val requestVacancySearch = RequestVacancySearch(id = vacancyId)
        val response = networkClient.doRequestGetVacancy(requestVacancySearch) as VacancyDto
        emit(Converter.fromVacancyDtoToVacancyDetails(response))
    }

    override suspend fun getSimilarVacancies(vacancyId: String): Flow<Vacancies> = flow {
        val requestSimilarVacancySearch = RequestSimilarVacancySearch(id = vacancyId)
        val response =
            networkClient.doRequestGetSimilarVacancies(requestSimilarVacancySearch) as ResponseVacanciesListDto
        emit(Converter.fromResponseVacanciesListDtoToVacancies(response))
    }
}
