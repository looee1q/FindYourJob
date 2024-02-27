package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.filter.FilterStorage
import ru.practicum.android.diploma.data.filter.SharedPrefFilterStorageImpl
import ru.practicum.android.diploma.data.network.HHApiService
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.repository.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.share.ExternalNavigatorImpl
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.share.ExternalNavigator

val dataModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences("FIND_YOUR_JOB_PREFERENCES", Context.MODE_PRIVATE)
    }

    single {
        Room
            .databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single<HHApiService> {
        Retrofit
            .Builder()
            .baseUrl("https://api.hh.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HHApiService::class.java)
    }

    single<ConnectivityManager> {
        androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    single<FilterStorage> {
        SharedPrefFilterStorageImpl(sharedPreferences = get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(hhApiService = get(), connectivityManager = get())
    }

    single<VacanciesRepository> {
        VacanciesRepositoryImpl(networkClient = get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }
}
