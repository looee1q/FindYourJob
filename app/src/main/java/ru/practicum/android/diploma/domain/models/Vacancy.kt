package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val id: String,
    val areaName: String,
    val employerLogoUrl: String,
    val employerName: String,
    val name: String,
    val salary: Salary,
)
