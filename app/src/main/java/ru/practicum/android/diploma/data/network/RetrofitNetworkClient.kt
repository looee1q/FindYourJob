package ru.practicum.android.diploma.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.RequestSimilarVacancySearch
import ru.practicum.android.diploma.data.dto.RequestVacanciesListSearch
import ru.practicum.android.diploma.data.dto.RequestVacancySearch
import ru.practicum.android.diploma.data.dto.Response

class RetrofitNetworkClient(
    private val hhApiService: HHApiService,
    private val connectivityManager: ConnectivityManager,
) : NetworkClient {

    override suspend fun doRequestSearchVacancies(requestDto: RequestVacanciesListSearch): Response {
        return if (!isConnected()) {
            Response().apply { resultCode = NO_INTERNET_CONNECTION }
        } else {
            withContext(Dispatchers.IO) {
                val options = formQueryMapToSearchVacancies(requestDto)
                val response = hhApiService.searchVacancies(options)
                response.apply { resultCode = SUCCESS_RESPONSE }
            }
        }
    }

    override suspend fun doRequestGetVacancy(dto: RequestVacancySearch): Response {
        return if (!isConnected()) {
            Response().apply { resultCode = NO_INTERNET_CONNECTION }
        } else {
            withContext(Dispatchers.IO) {
                val response = hhApiService.getVacancy(dto.id)
                response.apply { resultCode = SUCCESS_RESPONSE }
            }
        }
    }

    override suspend fun doRequestGetSimilarVacancies(dto: RequestSimilarVacancySearch): Response {
        return if (!isConnected()) {
            Response().apply { resultCode = NO_INTERNET_CONNECTION }
        } else {
            withContext(Dispatchers.IO) {
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

    private fun formQueryMapToSearchVacancies(
        dto: RequestVacanciesListSearch
    ): Map<String, String> = mutableMapOf(
        QUERY_MAP_KEY_PAGE to dto.page.toString(),
        QUERY_MAP_KEY_PER_PAGE to dto.perPage.toString(),
        QUERY_MAP_KEY_ONLY_WITH_SALARY to dto.onlyWithSalary.toString()
    ).apply {
        dto.text?.let { put(QUERY_MAP_KEY_TEXT, it) }
        dto.area?.let { put(QUERY_MAP_KEY_AREA, it) }
        dto.industry?.let { put(QUERY_MAP_KEY_INDUSTRY, it) }
        dto.currency?.let { put(QUERY_MAP_KEY_CURRENCY, it) }
        dto.salary?.let { put(QUERY_MAP_KEY_SALARY, it.toString()) }
    }

    companion object {
        const val NO_INTERNET_CONNECTION = -1
        const val SUCCESS_RESPONSE = 200

        const val QUERY_MAP_KEY_PAGE = "page"
        const val QUERY_MAP_KEY_PER_PAGE = "per_page"
        const val QUERY_MAP_KEY_ONLY_WITH_SALARY = "only_with_salary"
        const val QUERY_MAP_KEY_TEXT = "text"
        const val QUERY_MAP_KEY_AREA = "area"
        const val QUERY_MAP_KEY_INDUSTRY = "industry"
        const val QUERY_MAP_KEY_CURRENCY = "currency"
        const val QUERY_MAP_KEY_SALARY = "salary"
    }
}
