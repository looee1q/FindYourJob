package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDto(
    val id: String,
    val area: AreaResponseDto,
    val name: String,
    val employer: EmployerResponseDto,
    val salary: SalaryResponseDto,
    val contacts: ContactsResponseDto,
    val description: String,
    val employment: EmploymentResponseDto,
    val experience: ExperienceResponseDto,
    @SerializedName("key_skills")
    val keySkills: List<KeySkillsResponseDto>,
    @SerializedName("alternate_url")
    val alternateUrl: String,
    val schedule: ScheduleResponseDto,
) : Response()

data class AreaResponseDto(
    val id: String,
    val name: String
)

data class EmployerResponseDto(
    val id: String,
    val name: String,
    @SerializedName("logo_urls")
    val logoUrls: LogoUrlsResponseDto
)

data class LogoUrlsResponseDto(
    val original: String,
)

data class SalaryResponseDto(
    val currency: String,
    val from: Int,
    val to: Int
)

data class ContactsResponseDto(
    val email: String,
    val name: String,
    val phones: PhonesResponseDto

)

data class PhonesResponseDto(
    val city: String,
    val comment: String,
    val country: String,
    val number: String
)

data class EmploymentResponseDto(
    val name: String
)

data class ExperienceResponseDto(
    val name: String
)

data class KeySkillsResponseDto(
    val name: String
)

data class ScheduleResponseDto(
    val name: String
)
