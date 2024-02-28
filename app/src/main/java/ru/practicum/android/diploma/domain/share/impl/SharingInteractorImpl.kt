package ru.practicum.android.diploma.domain.share.impl

import ru.practicum.android.diploma.domain.share.ExternalNavigator
import ru.practicum.android.diploma.domain.share.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun openEmail(email: String) {
        externalNavigator.openEmail(email)
    }

    override fun openPhone(phoneNumber: String) {
        externalNavigator.openPhone(phoneNumber)
    }

    override fun shareVacancy(vacancyUrl: String) {
        externalNavigator.shareVacancy(vacancyUrl)
    }
}
