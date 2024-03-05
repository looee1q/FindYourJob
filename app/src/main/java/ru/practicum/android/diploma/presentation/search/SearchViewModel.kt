package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.state.SearchFragmentState
import ru.practicum.android.diploma.util.SearchResult
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(private val vacanciesInteractor: VacanciesInteractor) : ViewModel() {

    private var latestSearchText: String? = null
    private var currentRequest: VacanciesRequest? = null
    private val vacanciesList = ArrayList<Vacancy>()
    private var currentPage = 0
    private var maxPages = 0
    private var found = 0
    private var isNextPageLoading = false

    private val searchFragmentScreenState = MutableLiveData<SearchFragmentState>(SearchFragmentState.Start)
    private val searchDebounce = debounce<VacanciesRequest>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        makeRequest(it)
    }

    fun getSearchFragmentScreenState(): LiveData<SearchFragmentState> = searchFragmentScreenState

    fun searchByText(text: String) {
        if (text.isNotEmpty() && text != latestSearchText) {
            latestSearchText = text
            vacanciesList.clear()
            currentRequest = currentRequest?.copy(page = 0, text = text) ?: VacanciesRequest(text = text)
            searchDebounce(currentRequest!!)
        }
    }

    fun getNextPage() {
        if (currentRequest != null && !isNextPageLoading) {
            val nextPage = currentPage + 1
            if (nextPage <= maxPages) {
                currentRequest = currentRequest!!.copy(page = nextPage)
                isNextPageLoading = true
                makeRequest(currentRequest!!)
            }
        }
    }

    fun cancelSearch() {
        viewModelScope.coroutineContext.cancelChildren()
        latestSearchText = ""
        vacanciesList.clear()
        renderState(SearchFragmentState.Start)
    }

    fun getContent() {
        renderState(SearchFragmentState.Content(vacanciesList, found))
    }

    private fun makeRequest(vacanciesRequest: VacanciesRequest) {
        val isFirstPage = vacanciesRequest.page == 0
        renderState(SearchFragmentState.Loading(isFirstPage))
        viewModelScope.launch {
            vacanciesInteractor
                .searchVacancies(vacanciesRequest)
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

    private fun parsingResultSearch(result: SearchResult<Vacancies>, isFirstPage: Boolean) {
        when (result) {
            is SearchResult.NoInternet -> {
                renderState(SearchFragmentState.NoInternet(isFirstPage))
            }

            is SearchResult.Error -> {
                renderState(SearchFragmentState.Error(isFirstPage))
            }

            is SearchResult.Success -> {
                maxPages = result.data.pages
                currentPage = result.data.page
                found = result.data.found
                if (result.data.items.isEmpty()) {
                    renderState(SearchFragmentState.Empty)
                } else {
                    vacanciesList.addAll(filterDuplicateVacancy(result.data.items))
                    renderState(SearchFragmentState.Content(vacanciesList, found))
                }
            }
        }
    }

    private fun filterDuplicateVacancy(responseListVacancy: List<Vacancy>): List<Vacancy> {
        return responseListVacancy.filterNot { vacancy ->
            vacanciesList.any {
                vacancy.id == it.id
            }
        }
    }

    private fun renderState(state: SearchFragmentState) {
        searchFragmentScreenState.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
