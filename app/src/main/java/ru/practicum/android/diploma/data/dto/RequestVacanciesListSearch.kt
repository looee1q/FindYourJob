package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class RequestVacanciesListSearch(
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val text: String,
    val area: String,
    val parentArea: String,
    val industry: String,
    val currency: String,
    val salary: Int,
    val onlyWithSalary: Boolean = false,
)
