package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.util.SearchResult

interface FilterSearchRepository {

    fun getIndustries(): Flow<SearchResult<List<Industry>>>
}
