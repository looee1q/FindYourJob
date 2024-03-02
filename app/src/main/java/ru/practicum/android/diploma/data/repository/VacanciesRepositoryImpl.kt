package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.converters.Converter
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.dto.RequestSimilarVacancySearch
import ru.practicum.android.diploma.data.dto.RequestVacancySearch
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.SearchResult

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : VacanciesRepository {

    override suspend fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<SearchResult<Vacancies>> = flow {
        val requestVacanciesListSearch = Converter.fromVacanciesRequestToRequestVacanciesListSearch(vacanciesRequest)
        val response = networkClient.doRequestSearchVacancies(requestVacanciesListSearch)
        when (response.resultCode) {
            SUCCESS_RESPONSE -> {
                emit(
                    SearchResult.Success(
                        Converter.fromResponseVacanciesListDtoToVacancies(response as ResponseVacanciesListDto)
                    )
                )
            }

            NO_INTERNET_CONNECTION -> {
                emit(SearchResult.NoInternet())
            }

            else -> {
                emit(SearchResult.Error())
            }
        }
    }

    override suspend fun getVacancyDetails(vacancyId: String): Flow<SearchResult<VacancyDetails>> = flow {
        val requestVacancySearch = RequestVacancySearch(id = vacancyId)
        val response = networkClient.doRequestGetVacancy(requestVacancySearch)
        when (response.resultCode) {
            SUCCESS_RESPONSE -> {
                emit(SearchResult.Success(Converter.fromVacancyDtoToVacancyDetails(response as VacancyDto)))
            }
            NO_INTERNET_CONNECTION -> {
                emit(SearchResult.NoInternet())
            }
            else -> {
                emit(SearchResult.Error())
            }
        }
    }

    override suspend fun getVacancyDetailsFromLocalStorage(vacancyId: String): Flow<VacancyDetails> {
        return flow {
            emit(
                Converter.fromFavoriteVacancyEntityToVacancyDetails(
                    appDatabase.favoriteVacancyDao().getVacancyById(vacancyId)
                )
            )
        }
    }

    override suspend fun getSimilarVacancies(vacancyId: String): Flow<Vacancies> = flow {
        val requestSimilarVacancySearch = RequestSimilarVacancySearch(id = vacancyId)
        val response =
            networkClient.doRequestGetSimilarVacancies(requestSimilarVacancySearch) as ResponseVacanciesListDto
        emit(Converter.fromResponseVacanciesListDtoToVacancies(response))
    }

    override suspend fun addVacancyToFavorites(vacancy: VacancyDetails) {
        appDatabase.favoriteVacancyDao().insertVacancy(
            Converter.fromVacancyDetailsToFavoriteVacancyEntity(vacancy)
        )
    }

    override suspend fun removeVacancyFromFavorites(vacancyId: String) {
        appDatabase.favoriteVacancyDao().deleteVacancy(vacancyId)
    }

    override fun getFavoriteVacancies(): Flow<List<Vacancy>> {
        return appDatabase.favoriteVacancyDao().getAllVacancies().map {
            it.map { Converter.fromFavoriteVacancyEntityToVacancy(it) }
        }
    }

    companion object {
        private const val SUCCESS_RESPONSE = 200
        private const val NO_INTERNET_CONNECTION = -1
    }
}
