package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.presentation.search.state.SearchFragmentScreenState
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(private val vacanciesInteractor: VacanciesInteractor) : ViewModel() {

    private val searchFragmentScreenState = MutableLiveData<SearchFragmentScreenState>()
    private val searchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        makeRequest(it)
    }

    fun getSearchFragmentScreenState(): LiveData<SearchFragmentScreenState> = searchFragmentScreenState

    fun search(text: String) {
        searchDebounce(text)
    }

    private fun makeRequest(text: String) {
        if (text.isNotEmpty()) {
            renderSearchFragmentScreenState(SearchFragmentScreenState.Loading)
            val vacanciesRequest = VacanciesRequest(text = text)
            viewModelScope.launch {
                vacanciesInteractor
                    .searchVacancies(vacanciesRequest)
                    .collect {
                        if (it.items.isNotEmpty()) {
                            renderSearchFragmentScreenState(SearchFragmentScreenState.Content(it.items))
                        } else {
                            renderSearchFragmentScreenState(SearchFragmentScreenState.Empty)
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
