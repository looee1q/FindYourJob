package ru.practicum.android.diploma.domain.models

data class Vacancies(
    val items: List<VacancyBriefInfo>,
    val found: Int,
    val page: Int,
    val pages: Int
)
