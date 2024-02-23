package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.data.dto.Vacancy
import ru.practicum.android.diploma.databinding.ItemFoundVacancyBinding

class VacancyAdapter(
    private val vacancyList: ArrayList<Vacancy>,
    private val clickListener: VacancyClickListener
) : RecyclerView.Adapter<VacancyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFoundVacancyBinding.inflate(inflater, parent, false)
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancyList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return vacancyList.size
    }

    interface VacancyClickListener {
        fun onVacancyClick(vacancy: Vacancy)
    }
}
