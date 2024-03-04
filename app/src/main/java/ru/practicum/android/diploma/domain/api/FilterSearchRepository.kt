package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industry

interface FilterSearchRepository {

    fun getIndustries(): Flow<List<Industry>>

    fun getCountries(): Flow<List<Country>>
}
