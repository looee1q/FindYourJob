package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacanciesInteractorImpl(private val vacanciesRepository: VacanciesRepository) : VacanciesInteractor {

    override suspend fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<Vacancies> {
        return vacanciesRepository.searchVacancies(vacanciesRequest = vacanciesRequest)
    }

    override suspend fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails> {
        return vacanciesRepository.getVacancyDetails(vacancyId = vacancyId)
    }

    override suspend fun getSimilarVacancies(vacancyId: String): Flow<Vacancies> {
        return vacanciesRepository.getSimilarVacancies(vacancyId = vacancyId)
    }
}
