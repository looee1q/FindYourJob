package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface VacanciesInteractor {

    suspend fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<Vacancies>

    suspend fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails>

    suspend fun getSimilarVacancies(vacancyId: String): Flow<Vacancies>
}
