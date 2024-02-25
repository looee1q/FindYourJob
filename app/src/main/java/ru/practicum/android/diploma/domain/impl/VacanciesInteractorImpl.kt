package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy

class VacanciesInteractorImpl(private val vacanciesRepository: VacanciesRepository) : VacanciesInteractor {

    override suspend fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<List<Vacancy>> {
        return vacanciesRepository.searchVacancies(vacanciesRequest)
    }
}
