package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.RequestVacanciesListSearch
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy

object Converter {

    fun fromVacanciesRequestToRequestVacanciesListSearch(
        vacanciesRequest: VacanciesRequest
    ): RequestVacanciesListSearch {
        return RequestVacanciesListSearch(
            page = vacanciesRequest.page,
            perPage = vacanciesRequest.perPage,
            text = vacanciesRequest.text,
            area = vacanciesRequest.area,
            industry = vacanciesRequest.industry,
            currency = vacanciesRequest.currency,
            salary = vacanciesRequest.salary,
            onlyWithSalary = vacanciesRequest.onlyWithSalary,
        )
    }

    fun fromResponseVacanciesListDtoToVacancies(responseVacanciesListDto: ResponseVacanciesListDto): Vacancies {
        return Vacancies(
            found = responseVacanciesListDto.found,
            page = responseVacanciesListDto.page,
            pages = responseVacanciesListDto.pages,
            items = responseVacanciesListDto
                .listVacancies
                .map { vacancyResponse ->
                    Vacancy(
                        id = vacancyResponse.id,
                        areaName = vacancyResponse.area.name,
                        employerLogoUrl = vacancyResponse.employer.logoUrls?.original.orEmpty(),
                        employerName = vacancyResponse.employer.name,
                        name = vacancyResponse.name,
                        salary = Salary(
                            currency = vacancyResponse.salary?.currency,
                            from = vacancyResponse.salary?.from,
                            to = vacancyResponse.salary?.to
                        )
                    )
                }
                .toList()
        )
    }
}
