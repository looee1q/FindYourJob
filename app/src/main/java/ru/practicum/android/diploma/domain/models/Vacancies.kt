package ru.practicum.android.diploma.domain.models

data class Vacancies(
    val found: Int,
    val page: Int,
    val pages: Int,
    val items: List<VacancyBriefInfo>
)
