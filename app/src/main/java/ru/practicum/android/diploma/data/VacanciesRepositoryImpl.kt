package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.convertors.Convertor
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest

class VacanciesRepositoryImpl(private val networkClient: NetworkClient) : VacanciesRepository {
    override suspend fun searchVacancies(vacanciesRequest: VacanciesRequest): Vacancies {
        val response = networkClient.doRequestSearchVacancies(
            Convertor.fromVacanciesRequestToRequestVacanciesListSearch(vacanciesRequest)
        ) as ResponseVacanciesListDto
        return Convertor.fromResponseVacanciesListDtoToVacancies(response)
    }
}
