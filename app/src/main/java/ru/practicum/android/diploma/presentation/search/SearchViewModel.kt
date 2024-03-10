package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.state.SearchFragmentState
import ru.practicum.android.diploma.util.SearchResult
import ru.practicum.android.diploma.util.debounce

open class SearchViewModel(
    private val vacanciesInteractor: VacanciesInteractor,
    private val filterSearchInteractor: FilterSearchInteractor
) : ViewModel() {

    private var latestSearchText: String? = null
    private var currentRequest: VacanciesRequest? = null
    private val vacanciesList = mutableListOf<Vacancy>()
    private var currentPage = 0
    private var maxPages = 0
    private var found = 0
    private var isNextPageLoading = false

    open val searchFragmentScreenState = MutableLiveData<SearchFragmentState>(SearchFragmentState.Start)
    private val searchDebounce = debounce<VacanciesRequest>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        makeRequest(it)
    }

    private val _filterParameters = MutableLiveData<FilterParameters?>()
    val filterParameters: LiveData<FilterParameters?> = _filterParameters

    fun getSearchFragmentScreenState(): LiveData<SearchFragmentState> = searchFragmentScreenState

    fun search(text: String, applyFilters: Boolean = false) {
        if (text.isNotEmpty() && text != latestSearchText) {
            searchWithText(text = text)
        } else if (text.isNotEmpty() && applyFilters) {
            searchWithText(text = text)
        }
    }

    private fun searchWithText(text: String) {
        val area = filterParameters.value?.let {
            it.idRegion.ifEmpty {
                it.idCountry.ifEmpty {
                    null
                }
            }
        }
        val industry = filterParameters.value?.let {
            it.idIndustry.ifEmpty {
                null
            }
        }
        val salary = filterParameters.value?.salary.orEmpty()
        val onlyWithSalary = filterParameters.value?.doNotShowWithoutSalary
        latestSearchText = text
        vacanciesList.clear()
        currentRequest = currentRequest?.copy(
            page = 0,
            text = text,
            area = area,
            industry = industry,
            salary = if (salary.isEmpty()) null else salary.toInt(),
            onlyWithSalary = onlyWithSalary ?: false
        )
            ?: VacanciesRequest(
                text = text,
                area = area,
                industry = industry,
                salary = if (salary.isEmpty()) null else salary.toInt(),
                onlyWithSalary = onlyWithSalary ?: false
            )
        searchDebounce(currentRequest!!)
    }

    fun getNextPage() {
        if (currentRequest != null && !isNextPageLoading) {
            val nextPage = currentPage + 1
            if (nextPage < maxPages) {
                currentRequest = currentRequest?.copy(page = nextPage)
                isNextPageLoading = true
                currentRequest?.let { makeRequest(it) }
            }
        }
    }

    open fun cancelSearch() {
        viewModelScope.coroutineContext.cancelChildren()
        latestSearchText = ""
        vacanciesList.clear()
        renderState(SearchFragmentState.Start)
    }

    fun getContent() {
        renderState(SearchFragmentState.Content(vacanciesList, found))
    }

    open fun makeRequest(vacanciesRequest: VacanciesRequest) {
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

    fun getFilterParameters() {
        _filterParameters.value = filterSearchInteractor.getFilterParameters()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
