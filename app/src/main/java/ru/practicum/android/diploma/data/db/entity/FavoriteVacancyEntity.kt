package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_vacancy_table")
data class FavoriteVacancyEntity(
    @PrimaryKey
    val id: String,
    val areaId: String,
    val areaName: String,
    val name: String,
    val employerId: String,
    val employerName: String,
    val employerLogoUrls: String,
    val salaryCurrency: String,
    val salaryFrom: Int,
    val salaryGross: Boolean,
    val salaryTo: Int,
    val contactsEmail: String,
    val contactsName: String,
    val contactsPhonesCity: String,
    val contactsPhonesComment: String,
    val contactsPhonesCountry: String,
    val contactsPhonesNumber: String,
    val comment: String,
    val description: String,
    val employment: String,
    val experience: String,
    val keySkills: String,
    val alternateUrl: String,
    val schedule: String,
    val timeAddToFav: Long = System.currentTimeMillis(),
)
