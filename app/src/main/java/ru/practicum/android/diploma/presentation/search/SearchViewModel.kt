package ru.practicum.android.diploma.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.VacanciesRequest

class SearchViewModel(
    private val vacanciesInteractor: VacanciesInteractor
): ViewModel() {

    private val vacanciesRequest = VacanciesRequest(
        page = 0,
        text = "разработчик",
        area = "1",
        industry = null,
        currency = "RUR",
        salary = 300000,
        onlyWithSalary = true
    )

    fun makeRequest() {

        Log.d("SearchViewModel", "сейчас буду лаунчить!!!")

        viewModelScope.launch(Dispatchers.IO) {
            val searchVacancies = vacanciesInteractor.searchVacancies(vacanciesRequest)
            Log.d("SearchViewModel", "foundVacancies: $searchVacancies")
        }
    }
}
