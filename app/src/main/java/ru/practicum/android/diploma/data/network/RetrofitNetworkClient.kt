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

class RetrofitNetworkClient(private val hhApiService: HHApiService, private val context: Context) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response<out Any> {
        if (!isConnected()) {
            return Response<Any>().apply { resultCode = NO_INTERNET_CONNECTION }
        }

        if (dto !is RequestVacanciesListSearch && dto !is RequestVacancySearch && dto !is RequestSimilarVacancySearch) { // && dto !is RequestGetFiltersValues
            return Response<Any>().apply { resultCode = BAD_REQUEST }
        }

        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    is RequestVacanciesListSearch -> {
                        // заменить на реализацию options в RepositoryImpl при наличии фильтров
                        val options: Map<String, String> = emptyMap()
                        val response = hhApiService.searchVacancies(options.mapValues { dto.toString() })
                        response.apply { resultCode = SUCCESS_RESPONSE }
                    }

                    is RequestVacancySearch -> {
                        val response = hhApiService.getVacancy(dto.id)
                        response.apply { resultCode = SUCCESS_RESPONSE }
                    }

                    is RequestSimilarVacancySearch -> {
                        val response = hhApiService.getSimilarVacancies(dto.id)
                        response.apply { resultCode = SUCCESS_RESPONSE }
                    }

                    else -> {
                        // далее заменить на реализацию получения значений для фильтров
                        Response<Any>().apply { resultCode = BAD_REQUEST }
                    }
                }

            } catch (e: Throwable) {
                Response<Any>().apply { resultCode = UNEXPECTED_ERROR }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
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
