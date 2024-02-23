package ru.practicum.android.diploma.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.practicum.android.diploma.databinding.FragmentAboutBinding
import ru.practicum.android.diploma.ui.fragment.BindingFragment

class AboutFragment : BindingFragment<FragmentAboutBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private const val ARGS_TRACK = "ARGS_VACANCY_ID"

        fun createArgs(vacancyID: String): Bundle = bundleOf(ARGS_TRACK to vacancyID)

    }
}
