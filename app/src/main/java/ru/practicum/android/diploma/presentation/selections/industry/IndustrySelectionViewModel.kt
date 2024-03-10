package ru.practicum.android.diploma.presentation.selections.industry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.selections.industry.state.IndustrySelectionState
import ru.practicum.android.diploma.util.SearchResult

class IndustrySelectionViewModel(private val filterSearchInteractor: FilterSearchInteractor) : ViewModel() {

    private val industriesFragmentState = MutableLiveData<IndustrySelectionState>()
    private var checkedIndustry: Industry? = null
    private var latestCheckedPosition = -1
    private val industries = ArrayList<Industry>()

    init {
        getIndustries()
    }

    fun getIndustriesFragmentState(): LiveData<IndustrySelectionState> = industriesFragmentState

    fun saveCheckedIndustry(industry: Industry, position: Int) {
        checkedIndustry = industry
        industriesFragmentState.postValue(
            IndustrySelectionState.ChangeCheckedIndustry(
                industries,
                latestCheckedPosition,
                position
            )
        )
        latestCheckedPosition = position
    }

    fun saveIndustry() {
        checkedIndustry?.let {
            val filterParameters = filterSearchInteractor.getFilterParameters()
            if (filterParameters == null) {
                filterSearchInteractor.saveFilterParameters(
                    FilterParameters(idIndustry = it.id, nameIndustry = it.name)
                )
            } else {
                filterSearchInteractor.saveFilterParameters(
                    filterParameters.copy(idIndustry = it.id, nameIndustry = it.name)
                )
            }
        }
    }

    private fun getIndustries() {
        industriesFragmentState.postValue(IndustrySelectionState.Loading)
        viewModelScope.launch {
            filterSearchInteractor
                .getIndustries()
                .catch {
                    emit(SearchResult.Error())
                }
                .collect { result ->
                    when (result) {
                        is SearchResult.NoInternet -> {
                            industriesFragmentState.postValue(IndustrySelectionState.NoInternet)
                        }

                        is SearchResult.Error -> {
                            industriesFragmentState.postValue(IndustrySelectionState.Error)
                        }

                        is SearchResult.Success -> {
                            if (result.data.isEmpty()) {
                                industriesFragmentState.postValue(IndustrySelectionState.Error)
                            } else {
                                industries.addAll(result.data)
                                industriesFragmentState.postValue(IndustrySelectionState.Content(result.data))
                            }
                        }
                    }
                }
        }
    }
}
