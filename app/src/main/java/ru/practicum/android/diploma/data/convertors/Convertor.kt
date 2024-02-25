package ru.practicum.android.diploma.data.convertors

import ru.practicum.android.diploma.data.dto.RequestVacanciesListSearch
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.data.dto.SalaryResponse
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.VacancyBriefInfo

object Convertor {

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
            items = responseVacanciesListDto.listVacancies.map { fromVacancyResponseToVacancyBriefInfo(it) },
            found = responseVacanciesListDto.found,
            page = responseVacanciesListDto.page,
            pages = responseVacanciesListDto.pages
        )
    }

    private fun fromVacancyResponseToVacancyBriefInfo(vacancyResponse: VacancyResponse): VacancyBriefInfo {
        return VacancyBriefInfo(
            id = vacancyResponse.id,
            name = vacancyResponse.name,
            area = vacancyResponse.area.name,
            companyName = vacancyResponse.employer.name,
            companyLogoUrl = vacancyResponse.employer.logoUrls?.original,
            salary = fromSalaryResponseToSalary(vacancyResponse.salary)
        )
    }

    private fun fromSalaryResponseToSalary(salaryResponse: SalaryResponse?): Salary? {
        return if (salaryResponse == null) {
            null
        } else {
            Salary(
                currency = salaryResponse.currency,
                from = salaryResponse.from,
                to = salaryResponse.to
            )
        }
    }

}
