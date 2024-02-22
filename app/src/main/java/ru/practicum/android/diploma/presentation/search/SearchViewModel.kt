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
) : ViewModel() {

    // Класс с входными для имитации запроса
    private val vacanciesRequestMock = VacanciesRequest(
        page = 2,
        text = "разработчик",
        area = "1",
        industry = "5",
        currency = "RUR",
        salary = 240_000,
        onlyWithSalary = true
    )

    fun makeRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            val searchVacancies = vacanciesInteractor.searchVacancies(vacanciesRequestMock)
            // Лог для отображения найденных результатов по запросу
            Log.d("SearchViewModel", "foundVacancies: $searchVacancies")
        }
    }
}
