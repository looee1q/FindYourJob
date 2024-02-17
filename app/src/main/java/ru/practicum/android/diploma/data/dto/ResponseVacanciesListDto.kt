package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class ResponseVacanciesListDto(
    val found: Int,
    val pages: Int,
    val page: Int,
    @SerializedName("items")
    val listVacancies: ArrayList<Vacancy>
)

data class Vacancy(
    val id: String,
    val area: AreaResponse,
    val name: String,
    val employer: EmployerResponse,
    val salary: SalaryResponse,
)

data class AreaResponse(
    val id: String,
    val name: String
)

data class EmployerResponse(
    val id: String,
    val name: String,
    val logo_urls: LogoUrlsResponse
)

data class LogoUrlsResponse(
    val original: String,
)

data class SalaryResponse(
    val currency: String,
    val from: Int,
    val gross: Boolean,
    val to: Int
)
