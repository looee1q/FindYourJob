package ru.practicum.android.diploma.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.favorite.state.FavoritesState

class FavoriteViewModel(private val vacanciesInteractor: VacanciesInteractor) : ViewModel() {

    private val favorites = arrayListOf<Vacancy>()

    private val _favoritesStateLiveData = MutableLiveData<FavoritesState>()
    val favoritesStateLiveData: LiveData<FavoritesState> get() = _favoritesStateLiveData

    private val _favoritesLiveData = MutableLiveData(favorites)
    val favoritesLiveData: LiveData<ArrayList<Vacancy>> get() = _favoritesLiveData

    init {
        getFavoriteVacancies()
    }

    private fun getFavoriteVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            vacanciesInteractor.getFavoriteVacancies()
                .onEach {
                    favorites.clear()
                    if (it.isEmpty()) {
                        _favoritesStateLiveData.postValue(FavoritesState.Empty)
                    } else {
                        favorites.addAll(it)
                        _favoritesStateLiveData.postValue(FavoritesState.Content(it))
                    }
                }
                .catch { _favoritesStateLiveData.postValue(FavoritesState.Error) }
                .collect { }
        }
    }

}
