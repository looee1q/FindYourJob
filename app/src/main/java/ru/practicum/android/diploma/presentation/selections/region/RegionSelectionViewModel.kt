package ru.practicum.android.diploma.presentation.selections.region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.selections.region.state.RegionSelectionState
import ru.practicum.android.diploma.util.SearchResult
import ru.practicum.android.diploma.util.debounce

class RegionSelectionViewModel(
    private val filterSearchInteractor: FilterSearchInteractor
) : ViewModel() {

    private val _regionsStateLiveData = MutableLiveData<RegionSelectionState>()

    val regionsStateLiveData: LiveData<RegionSelectionState> get() = _regionsStateLiveData

    private val searchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        getParentRegionsWithDebounce2(it)
    }

    private val listOfAllRegions = mutableListOf<Region>()

    fun cancelSearch() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    fun getRegions() {
        viewModelScope.launch(Dispatchers.IO) {
            _regionsStateLiveData.postValue(RegionSelectionState.Loading)
            filterSearchInteractor.getRegions()
                .catch {
                    _regionsStateLiveData.postValue(RegionSelectionState.Error)
                }
                .collect {
                    handleSearchResult(it)
                    if (listOfAllRegions.isEmpty() && it is SearchResult.Success) {
                        listOfAllRegions.addAll(it.data)
                    }
                }
        }
    }

    fun getParentRegionsWithDebounce(region: String) {
        searchDebounce.invoke(region)
    }

    fun getParentRegionsWithDebounce2(region: String) {
        if (listOfAllRegions.isEmpty()) {
            getRegions()
        } else if (!listOfAllRegions.map { it.name }.contains(region)) {
            _regionsStateLiveData.postValue(RegionSelectionState.Empty)
        } else {
            listOfAllRegions.first { it.name == region }.also {
                getParentRegions(it.id)
            }
        }
    }

    private fun getParentRegions(parentId: String) {
        _regionsStateLiveData.postValue(RegionSelectionState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            filterSearchInteractor.getParentRegions(parentId)
                .catch {
                    _regionsStateLiveData.postValue(RegionSelectionState.Error)
                }
                .collect {
                    handleSearchResult(it)
                }
        }
    }

    private fun handleSearchResult(searchResult: SearchResult<List<Region>>) {
        when (searchResult) {
            is SearchResult.Success -> {
                if (searchResult.data.isEmpty()) {
                    _regionsStateLiveData.postValue(RegionSelectionState.Empty)
                } else {
                    _regionsStateLiveData.postValue(
                        RegionSelectionState.Content(searchResult.data)
                    )
                }
            }

            is SearchResult.Error -> {
                _regionsStateLiveData.postValue(RegionSelectionState.Error)
            }

            is SearchResult.NoInternet -> {
                _regionsStateLiveData.postValue(RegionSelectionState.NoInternet)
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}
