package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class AreaDTO(
    val areas: List<AreaDTO>,
    val id: String,
    val name: String,
    @SerializedName("parent_id")
    val parentId: String?
)
