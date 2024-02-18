package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun doRequestSearchVacancies(dto: Any): Response<out Any>
    suspend fun doRequestGetVacancy(dto: Any): Response<out Any>
    suspend fun doRequestGetSimilarVacancies(dto: Any): Response<out Any>
}
