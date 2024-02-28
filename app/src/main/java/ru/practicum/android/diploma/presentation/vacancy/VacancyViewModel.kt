package ru.practicum.android.diploma.presentation.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.share.SharingInteractor
import ru.practicum.android.diploma.presentation.vacancy.state.VacancyFragmentScreenState

class VacancyViewModel(
    private val vacanciesInteractor: VacanciesInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val vacancyFragmentScreenState = MutableLiveData<VacancyFragmentScreenState>()
    fun getVacancyFragmentScreenState(): LiveData<VacancyFragmentScreenState> = vacancyFragmentScreenState

    fun getVacancyDetails(text: String) {
        if (text.isNotEmpty()) {
            renderVacancyFragmentScreenState(VacancyFragmentScreenState.Loading)
            viewModelScope.launch {
                vacanciesInteractor
                    .getVacancyDetails(text)
                    .collect {
                        renderVacancyFragmentScreenState(VacancyFragmentScreenState.Content(it))
                        setInitialVacancyFavoriteStatus(it)
                    }
            }
        }
    }

    private fun renderVacancyFragmentScreenState(state: VacancyFragmentScreenState) {
        vacancyFragmentScreenState.postValue(state)
    }

    fun getStringKeySkills(keySkills: List<String>): String {
        val sb = StringBuilder()

        keySkills.forEachIndexed { _, skill ->
            sb.append("• $skill\n")
        }
        return sb.toString()
    }

    fun shareVacancy(vacancyUrl: String?) {
        if (vacancyUrl != null) {
            sharingInteractor.shareVacancy(vacancyUrl)
        }
    }

    fun openEmail(email: String?) {
        if (email != null) {
            sharingInteractor.openEmail(email)
        }
    }

    fun openPhone(phoneNumber: String?) {
        if (phoneNumber != null) {
            sharingInteractor.openPhone(phoneNumber)
        }
    }

    fun addVacancyToFavorites(vacancyDetails: VacancyDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            vacanciesInteractor.addVacancyToFavorites(vacancyDetails)
        }
        renderVacancyFragmentScreenState(
            VacancyFragmentScreenState.Content(vacancyDetails.copy(isFavorite = true))
        )
    }

    fun removeVacancyFromFavorites(vacancyDetails: VacancyDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            vacanciesInteractor.removeVacancyFromFavorites(vacancyDetails.id)
        }
        renderVacancyFragmentScreenState(
            VacancyFragmentScreenState.Content(vacancyDetails.copy(isFavorite = false))
        )
    }

    private fun setInitialVacancyFavoriteStatus(vacancyDetails: VacancyDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            vacanciesInteractor.getFavoriteVacancies().collect {
                val vacanciesId = it.map { it.id } //вынести мапу наверх
                if (vacanciesId.contains(vacancyDetails.id)) {
                    renderVacancyFragmentScreenState(
                        VacancyFragmentScreenState.Content(vacancyDetails.copy(isFavorite = true))
                    )
                }
            }
        }
    }

}
