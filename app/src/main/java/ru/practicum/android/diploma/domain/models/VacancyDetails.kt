package ru.practicum.android.diploma.domain.models

/**
 * Детали вакансии.
 *
 * @param id идентификатор вакансии
 * @param alternateUrl ссылка на представление вакансии на сайте
 * @param areaName название региона
 * @param description описание в html, не менее 200 символов
 * @param employerName название компании
 * @param employment тип занятости
 * @param experience опыт работы
 * @param keySkills список ключевых навыков
 * @param name название вакансии
 * @param salary зарплата
 * @param schedule график работы
 *
 * @author Иванов Павел Александрович
 */
data class VacancyDetails(
    val id: String,
    val alternateUrl: String,
    val areaName: String,
    val description: String,
    val employerName: String,
    val employment: String,
    val experience: String,
    val keySkills: List<String>,
    val name: String,
    val salary: Salary,
    val schedule: String
)
