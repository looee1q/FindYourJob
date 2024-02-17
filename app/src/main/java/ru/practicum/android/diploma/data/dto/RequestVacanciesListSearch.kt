package ru.practicum.android.diploma.data.dto

data class RequestVacanciesListSearch(
    val page: Int,
    val per_page: Int,
    val text: String,
    val area: String,
    val parentArea: String,
    val industry: String,
    val currency: String,
    val salary: Int,
    val onlyWithSalary:Boolean = false,
)
