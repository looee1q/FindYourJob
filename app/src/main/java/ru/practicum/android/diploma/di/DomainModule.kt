package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.impl.FilterSearchInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacanciesInteractorImpl
import ru.practicum.android.diploma.domain.share.SharingInteractor
import ru.practicum.android.diploma.domain.share.impl.SharingInteractorImpl

val domainModule = module {

    factory<VacanciesInteractor> {
        VacanciesInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<FilterSearchInteractor> {
        FilterSearchInteractorImpl(get())
    }
}
