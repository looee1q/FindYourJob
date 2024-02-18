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
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response<out Any> {
        if (!isConnected()) {
            return Response<Any>().apply { resultCode = -1 }
        }
        if ((dto !is RequestVacanciesListSearch)
            && (dto !is RequestVacancySearch)
            && (dto !is RequestSimilarVacancySearch)
            //&& (dto !is RequestGetFiltersValues)
        ) {
            return Response<Any>().apply { resultCode = 400 }
        }

        try {
            return withContext(Dispatchers.IO)
            {
                when (dto) {
                    is RequestVacanciesListSearch -> {
                        val options: Map<String, String> = emptyMap() //заменить на реализацию options в RepositoryImpl при наличии фильтров
                        val response = hhApiService.searchVacancies(options.mapValues { dto.toString() })
                        response.apply { resultCode = 200 }
                    }

                    is RequestVacancySearch -> {
                        val response = hhApiService.getVacancy(dto.id)
                        response.apply { resultCode = 200 }
                    }

                    is RequestSimilarVacancySearch -> {
                        val response = hhApiService.getSimilarVacancies(dto.id)
                        response.apply { resultCode = 200 }
                    }
                    else -> {
                        // далее заменить на реализацию получения значений для фильтров
                        Response<Any>().apply { resultCode = 400 }
                    }
                }
            }
        } catch (e: Throwable) {
            return withContext(Dispatchers.IO){Response<Any>().apply { resultCode = 500 }}
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}
