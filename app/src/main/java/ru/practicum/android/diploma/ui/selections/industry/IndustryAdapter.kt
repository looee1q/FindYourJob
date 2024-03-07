package ru.practicum.android.diploma.ui.selections.industry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustrySelectionBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustryAdapter(
    private val itemClickListener: IndustryClickListener
) : RecyclerView.Adapter<IndustryViewHolder>() {

    private val industries = mutableListOf<Industry>()
    private val originalList: MutableList<Industry> = ArrayList()
    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIndustrySelectionBinding.inflate(inflater, parent, false)
        return IndustryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(industries[position], position == selectedPosition)

        holder.binding.rbIndustryName.setOnCheckedChangeListener { _, b ->
            if (b) {
                itemClickListener.onIndustryClick(industries[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return industries.size
    }

    fun setIndustryList(newIndustryList: List<Industry>, oldPos: Int, newPos: Int) {
        if (industries.isEmpty()) {
            industries.addAll(newIndustryList)
        }
        selectedPosition = newPos
        if (newPos == -1) {
            notifyItemRangeInserted(0, industries.size - 1)
        }
        if (oldPos > -1) {
            notifyItemChanged(oldPos)
        }
        originalList.clear()
        originalList.addAll(newIndustryList)
    }

    fun filter(searchQuery: String?) {
        if (searchQuery.isNullOrEmpty()) {
            updateDisplayList(originalList)
        } else {
            val filteredList = originalList.filter {
                it.name.contains(searchQuery, true)
            }
            updateDisplayList(filteredList)
        }
    }

    private fun updateDisplayList(updatedList: List<Industry>) {
        industries.clear()
        industries.addAll(updatedList)
        notifyDataSetChanged()
    }

    interface IndustryClickListener {
        fun onIndustryClick(industry: Industry, position: Int)
    }
}
