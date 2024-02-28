package ru.practicum.android.diploma.ui.vacancy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemPhoneCommentBinding
import ru.practicum.android.diploma.domain.models.Phone

class PhoneAndCommentAdapter(
    private val phonesList: List<Phone>?,
    private val clickListener: PhoneClickListener?
) : RecyclerView.Adapter<PhoneAndCommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneAndCommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPhoneCommentBinding.inflate(inflater, parent, false)
        return PhoneAndCommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhoneAndCommentViewHolder, position: Int) {
        holder.bind(phonesList?.get(position), clickListener)
    }

    override fun getItemCount(): Int {
        return phonesList?.size ?: 0
    }

    interface PhoneClickListener {
        fun onPhoneClick(phone: Phone?)
    }
}
