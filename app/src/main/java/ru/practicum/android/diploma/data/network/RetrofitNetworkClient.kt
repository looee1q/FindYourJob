package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RequestSimilarVacancySearch
import ru.practicum.android.diploma.data.dto.RequestVacanciesListSearch
import ru.practicum.android.diploma.data.dto.RequestVacancySearch
import ru.practicum.android.diploma.data.dto.Response

class RetrofitNetworkClient(
    private val hhApiService: HHApiService,
    private val context: Context,
    private val connectivityManager: ConnectivityManager,
) : NetworkClient {

    override suspend fun doRequestSearchVacancies(dto: RequestVacanciesListSearch): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NO_INTERNET_CONNECTION }
        } else {
            return withContext(Dispatchers.IO) {
                // заменить на реализацию options в RepositoryImpl при наличии фильтров
                val options: Map<String, String> = emptyMap()
                val response = hhApiService.searchVacancies(options.mapValues { dto.toString() })
                response.apply { resultCode = SUCCESS_RESPONSE }
            }
        }
    }

    override suspend fun doRequestGetVacancy(dto: RequestVacancySearch): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NO_INTERNET_CONNECTION }
        } else {
            return withContext(Dispatchers.IO) {
                val response = hhApiService.getVacancy(dto.id)
                response.apply { resultCode = SUCCESS_RESPONSE }
            }
        }
    }

    override suspend fun doRequestGetSimilarVacancies(dto: RequestSimilarVacancySearch): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NO_INTERNET_CONNECTION }
        } else {
            return withContext(Dispatchers.IO) {
                val response = hhApiService.getSimilarVacancies(dto.id)
                response.apply { resultCode = SUCCESS_RESPONSE }
            }
        }
    }

    private fun isConnected(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> {
                    false
                }
            }
        }
        return false
    }

    companion object {
        var NO_INTERNET_CONNECTION = -1
        var SUCCESS_RESPONSE = 200
        var BAD_REQUEST = 400
        var UNEXPECTED_ERROR = 500
    }
}
