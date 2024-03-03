package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.api.FilterSearchRepository
import ru.practicum.android.diploma.domain.models.Industry

class FilterSearchInteractorImpl(private val filterSearchRepository: FilterSearchRepository) : FilterSearchInteractor {
    override fun getIndustries(): Flow<List<Industry>> {
        return filterSearchRepository.getIndustries()
    }

}
