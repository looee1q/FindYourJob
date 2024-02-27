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
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.presentation.search.state.SearchFragmentScreenState
import ru.practicum.android.diploma.util.SearchResult
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(private val vacanciesInteractor: VacanciesInteractor) : ViewModel() {

    private var latestSearchText: String? = null
    private val searchFragmentScreenState = MutableLiveData<SearchFragmentScreenState>(SearchFragmentScreenState.Start)
    private val searchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        makeRequest(it)
    }

    fun getSearchFragmentScreenState(): LiveData<SearchFragmentScreenState> = searchFragmentScreenState

    fun search(text: String) {
        searchDebounce(text)
    }

    fun cancelSearch() {
        viewModelScope.coroutineContext.cancelChildren()
        latestSearchText = ""
        renderSearchFragmentScreenState(SearchFragmentScreenState.Start)
    }

    private fun makeRequest(text: String) {
        if (text.isNotEmpty() && text != latestSearchText) {
            latestSearchText = text
            renderSearchFragmentScreenState(SearchFragmentScreenState.Loading)
            val vacanciesRequest = VacanciesRequest(text = text)
            viewModelScope.launch {
                vacanciesInteractor
                    .searchVacancies(vacanciesRequest)
                    .catch {
                        emit(SearchResult.Error())
                    }
                    .collect { result ->
                        if (isActive) {
                            when (result) {
                                is SearchResult.NoInternet -> {
                                    renderSearchFragmentScreenState(SearchFragmentScreenState.NoInternet)
                                }

                                is SearchResult.Error -> {
                                    renderSearchFragmentScreenState(SearchFragmentScreenState.Error)
                                }

                                is SearchResult.Success -> {
                                    if (result.data != null) {
                                        if (result.data.items.isEmpty()) {
                                            renderSearchFragmentScreenState(SearchFragmentScreenState.Empty)
                                        } else {
                                            renderSearchFragmentScreenState(SearchFragmentScreenState.Content(result.data))
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun renderSearchFragmentScreenState(state: SearchFragmentScreenState) {
        searchFragmentScreenState.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
