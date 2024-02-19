package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
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

    // @GET("/industries")
    // suspend fun getIndustries(): Response<List<CountriesDTO>>

    // @GET("/areas/countries")
    // suspend fun getCountries(): Response<List<CountriesDTO>>

    // ?@GET("/areas/{area_id}")
    // ?suspend fun getAreasById(@Path("area_id") id: String): Response<List<AreasDTO>>

    // @GET("/areas/")
    // suspend fun getAreas(): Response<List<AreasDTO>>
}
