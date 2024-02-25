package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy

interface VacanciesRepository {

    suspend fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<List<Vacancy>>
}
