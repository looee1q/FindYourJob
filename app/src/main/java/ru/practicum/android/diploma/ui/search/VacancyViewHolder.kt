package ru.practicum.android.diploma.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemFoundVacancyBinding

class VacancyViewHolder(
    private val binding: ItemFoundVacancyBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: VacancyBriefInfo) {
        binding.vacancyName.text = createVacancyTitle(vacancy.name, vacancy.area)
        binding.employerName.text = vacancy.companyName
        binding.salary.text = createVacancySalary(vacancy.salary)

        Glide
            .with(itemView)
            .load(vacancy.companyLogoUrl)
            .placeholder(R.drawable.ic_placeholder_vacancy_item)
            .centerCrop()
            .transform(RoundedCorners(binding.employerLogo.resources.getDimensionPixelSize(R.dimen._12dp)))
            .into(binding.employerLogo)

        itemView.setOnClickListener { clickListener.onVacancyClick(vacancy) }
    }

    private fun createVacancyTitle(name: String, place: String): String {
        val vacancyTitle = ""
        return vacancyTitle
    }

    private fun createVacancySalary(salary: Salary?): String {
        val vacancySalary = ""
        return vacancySalary
    }
}
