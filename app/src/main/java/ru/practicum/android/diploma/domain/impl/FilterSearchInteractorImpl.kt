package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.api.FilterSearchRepository
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.util.SearchResult

class FilterSearchInteractorImpl(private val filterSearchRepository: FilterSearchRepository) : FilterSearchInteractor {
    override fun getIndustries(): Flow<SearchResult<List<Industry>>> {
        return filterSearchRepository.getIndustries()
    }

}
