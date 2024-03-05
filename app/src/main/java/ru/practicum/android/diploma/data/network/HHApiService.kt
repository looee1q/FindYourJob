package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.AreaDTO
import ru.practicum.android.diploma.data.dto.CountryDto
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.data.dto.VacancyDto

interface HHApiService {

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Find Your Job/1.0 (goaltenders@yandex.ru)"
    )
    @GET("/vacancies")
    suspend fun searchVacancies(@QueryMap options: Map<String, String>): ResponseVacanciesListDto

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Find Your Job/1.0 (goaltenders@yandex.ru)"
    )
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): VacancyDto

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Find Your Job/1.0 (goaltenders@yandex.ru)"
    )
    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun getSimilarVacancies(@Path("vacancy_id") id: String): ResponseVacanciesListDto

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Find Your Job/1.0 (goaltenders@yandex.ru)"
    )
    @GET("/industries")
    suspend fun getIndustries(): List<IndustryDto>

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Find Your Job/1.0 (goaltenders@yandex.ru)"
    )
    @GET("/areas/countries")
    suspend fun getCountries(): List<CountryDto>

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Find Your Job/1.0 (goaltenders@yandex.ru)"
    )
    @GET("/areas")
    suspend fun getAreas(): List<AreaDTO>

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Find Your Job/1.0 (goaltenders@yandex.ru)"
    )
    @GET("/areas/{area_id}")
    suspend fun getAreas(@Path("area_id") id: String): AreaDTO
}
