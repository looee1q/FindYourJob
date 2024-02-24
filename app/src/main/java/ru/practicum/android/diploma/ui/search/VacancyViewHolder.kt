package ru.practicum.android.diploma.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.SalaryResponse
import ru.practicum.android.diploma.data.dto.Vacancy
import ru.practicum.android.diploma.databinding.ItemFoundVacancyBinding

class VacancyViewHolder(
    private val binding: ItemFoundVacancyBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: Vacancy, vacancyClickListener: VacancyAdapter.VacancyClickListener?) {
        binding.vacancyName.text = createVacancyTitle(vacancy.name, vacancy.area.name)
        binding.employerName.text = vacancy.employer.name
        binding.salary.text = createVacancySalary(vacancy.salary)

        Glide
            .with(itemView)
            .load(vacancy.employer.logoUrls)
            .placeholder(R.drawable.ic_placeholder_vacancy_item)
            .centerCrop()
            .transform(RoundedCorners(binding.employerLogo.resources.getDimensionPixelSize(R.dimen._12dp)))
            .into(binding.employerLogo)

        itemView.setOnClickListener {
            vacancyClickListener?.onVacancyClick(vacancy)
        }

    }

    private fun createVacancyTitle(name: String, place: String): String {
        return "$name, $place"
    }

    private fun createVacancySalary(salary: SalaryResponse?): String {
        return itemView.context.getString(R.string.salary_from_to, salary?.from.toString(), salary?.to.toString())
    }
}
