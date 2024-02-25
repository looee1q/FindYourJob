package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest

interface VacanciesInteractor {

    suspend fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<Vacancies>
}
