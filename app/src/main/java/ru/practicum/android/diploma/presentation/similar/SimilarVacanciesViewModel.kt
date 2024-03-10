package ru.practicum.android.diploma.presentation.similar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.search.state.SearchFragmentState
import ru.practicum.android.diploma.util.SearchResult

class SimilarVacanciesViewModel(
    private val vacanciesInteractor: VacanciesInteractor,
    private val filterSearchInteractor: FilterSearchInteractor
) : SearchViewModel(vacanciesInteractor, filterSearchInteractor) {

    private var similarVacancyId = ""
    private var isRequestMade = false
    override val searchFragmentScreenState = MutableLiveData<SearchFragmentState>()

    fun getSimilarVacancies(vacancyId: String) {
        if (!isRequestMade && vacancyId.isNotEmpty()) {
            isRequestMade = true
            similarVacancyId = vacancyId
            currentRequest = VacanciesRequest()
            makeRequest(currentRequest!!)
        }
    }

    override fun makeRequest(vacanciesRequest: VacanciesRequest) {
        val isFirstPage = vacanciesRequest.page == 0
        renderState(SearchFragmentState.Loading(isFirstPage))
        viewModelScope.launch {
            vacanciesInteractor
                .getSimilarVacancies(similarVacancyId, vacanciesRequest)
                .catch {
                    emit(SearchResult.Error())
                }
                .collect { result ->
                    if (isActive) {
                        parsingResultSearch(result, isFirstPage)
                        isNextPageLoading = false
                    }
                }
        }
    }

    override fun cancelSearch() {
        isRequestMade = true
    }
}
