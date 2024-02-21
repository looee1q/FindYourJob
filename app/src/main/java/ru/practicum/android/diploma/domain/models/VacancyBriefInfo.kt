package ru.practicum.android.diploma.domain.models

data class VacancyBriefInfo(
    val id: String,
    val name: String,
    val area: String,
    val companyName: String,
    val companyLogoUrl: String?,
    val salary: Salary?
)
