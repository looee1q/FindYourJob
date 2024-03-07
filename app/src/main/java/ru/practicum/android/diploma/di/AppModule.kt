package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.favorite.FavoriteViewModel
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.selections.area.AreaSelectionViewModel
import ru.practicum.android.diploma.presentation.selections.country.CountrySelectionViewModel
import ru.practicum.android.diploma.presentation.selections.region.RegionSelectionViewModel
import ru.practicum.android.diploma.presentation.selections.industry.IndustrySelectionViewModel
import ru.practicum.android.diploma.presentation.similar.SimilarVacanciesViewModel
import ru.practicum.android.diploma.presentation.selections.region.RegionSelectionViewModel
import ru.practicum.android.diploma.presentation.vacancy.VacancyViewModel

val appModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(vacanciesInteractor = get())
    }

    viewModel<FavoriteViewModel> {
        FavoriteViewModel(vacanciesInteractor = get())
    }

    viewModel<VacancyViewModel> {
        VacancyViewModel(vacanciesInteractor = get(), sharingInteractor = get())
    }

    viewModel<SimilarVacanciesViewModel> {
        SimilarVacanciesViewModel(vacanciesInteractor = get())
    }

    viewModel<IndustrySelectionViewModel> {
        IndustrySelectionViewModel(filterSearchInteractor = get())
    }

    viewModel<CountrySelectionViewModel> {
        CountrySelectionViewModel(filterSearchInteractor = get())
    }

    viewModel<AreaSelectionViewModel> {
        AreaSelectionViewModel(filterSearchInteractor = get())
    }

    viewModel<RegionSelectionViewModel> {
        RegionSelectionViewModel(filterSearchInteractor = get())
    }
}
