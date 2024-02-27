package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.SearchResult

interface VacanciesInteractor {

    suspend fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<SearchResult<Vacancies>>

    suspend fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails>

    suspend fun getSimilarVacancies(vacancyId: String): Flow<Vacancies>
}
