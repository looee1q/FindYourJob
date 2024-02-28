package ru.practicum.android.diploma.domain.models

/**
 * Детали вакансии.
 *
 * @param id идентификатор вакансии
 * @param address адрес
 * @param alternateUrl ссылка на представление вакансии на сайте
 * @param areaName название региона
 * @param contactEmail электронная почта контакта
 * @param contactName имя контакта
 * @param contactPhones телефон контакта
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
    val address: String,
    val alternateUrl: String,
    val areaName: String,
    val contactEmail: String?,
    val contactName: String?,
    val contactPhones: List<Phone>?,
    val description: String,
    val employerLogoUrl: String,
    val employerName: String,
    val employment: String,
    val experience: String,
    val keySkills: List<String>,
    val name: String,
    val salary: Salary,
    val schedule: String,
    val isFavorite: Boolean = false
)
