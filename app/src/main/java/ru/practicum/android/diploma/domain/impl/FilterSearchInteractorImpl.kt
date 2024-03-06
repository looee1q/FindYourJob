package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.api.FilterSearchRepository
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.util.SearchResult

class FilterSearchInteractorImpl(private val filterSearchRepository: FilterSearchRepository) : FilterSearchInteractor {

    override fun getIndustries(): Flow<SearchResult<List<Industry>>> {
        return filterSearchRepository.getIndustries()
    }

    override fun getCountries(): Flow<SearchResult<List<Country>>> {
        return filterSearchRepository.getCountries()
    }

    override fun getRegions(): Flow<SearchResult<List<Region>>> {
        return filterSearchRepository.getRegions()
    }

    override fun getParentRegions(parentAreaId: String): Flow<SearchResult<List<Region>>> {
        return filterSearchRepository.getParentRegions(parentAreaId)
    }
}
