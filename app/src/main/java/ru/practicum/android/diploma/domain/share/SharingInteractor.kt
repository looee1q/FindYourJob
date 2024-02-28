package ru.practicum.android.diploma.domain.share

interface SharingInteractor {
    fun openEmail(email: String)
    fun openPhone(phoneNumber: String)
    fun shareVacancy(vacancyUrl: String)
}
