package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_vacancy_table")
data class FavoriteVacancyEntity(
    @PrimaryKey
    val id: String,
    val address: String,
    val areaName: String,
    val name: String,
    val employerName: String,
    val employerLogoUrl: String,
    val salaryCurrency: String,
    val salaryFrom: Int,
    val salaryTo: Int,
    val contactsEmail: String,
    val contactsName: String,
    val contactsPhonesInJson: String,
    val description: String,
    val employment: String,
    val experience: String,
    val keySkillsInJson: String,
    val alternateUrl: String,
    val schedule: String,
    val timeAddToFav: Long = System.currentTimeMillis(),
)
