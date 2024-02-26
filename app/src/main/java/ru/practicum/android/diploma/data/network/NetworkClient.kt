package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.RequestSimilarVacancySearch
import ru.practicum.android.diploma.data.dto.RequestVacanciesListSearch
import ru.practicum.android.diploma.data.dto.RequestVacancySearch
import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {

    suspend fun doRequestSearchVacancies(dto: RequestVacanciesListSearch): Response

    suspend fun doRequestGetVacancy(dto: RequestVacancySearch): Response

    suspend fun doRequestGetSimilarVacancies(dto: RequestSimilarVacancySearch): Response
}
