package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.converters.Converter
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy

class VacanciesRepositoryImpl(private val networkClient: NetworkClient) : VacanciesRepository {

    override suspend fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<List<Vacancy>> = flow {
        val requestVacanciesListSearch = Converter.fromVacanciesRequestToRequestVacanciesListSearch(vacanciesRequest)
        val response = networkClient.doRequestSearchVacancies(requestVacanciesListSearch) as ResponseVacanciesListDto
        emit(Converter.fromResponseVacanciesListDtoToVacanciesList(response))
    }
}
