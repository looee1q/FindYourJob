package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.converters.Converter
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest

class VacanciesRepositoryImpl(private val networkClient: NetworkClient) : VacanciesRepository {

    override suspend fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<Vacancies> = flow {
        val requestVacanciesListSearch = Converter.fromVacanciesRequestToRequestVacanciesListSearch(vacanciesRequest)
        val response = networkClient.doRequestSearchVacancies(requestVacanciesListSearch) as ResponseVacanciesListDto
        emit(Converter.fromResponseVacanciesListDtoToVacancies(response))
    }
}
