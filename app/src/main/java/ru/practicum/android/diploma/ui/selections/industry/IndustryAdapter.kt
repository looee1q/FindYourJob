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
    }

    override fun getItemCount(): Int {
        return industries.size
    }

    interface IndustryClickListener {
        fun onIndustryClick(industry: Industry, position: Int)
    }
}
