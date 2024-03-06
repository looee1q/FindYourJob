package ru.practicum.android.diploma.ui.similar

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.similar.SimilarVacanciesViewModel
import ru.practicum.android.diploma.ui.search.SearchFragment
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment

class SimilarVacanciesFragment : SearchFragment() {

    override val viewModel by viewModel<SimilarVacanciesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSimilarVacancies(
            requireArguments().getString(ARGS_SIMILAR_VACANCY_ID).toString()
        )

        showActualViews()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showActualViews() {
        with(binding) {
            search.isVisible = false
            filters.isVisible = false
            InputEditText.isVisible = false
            icClose.isVisible = false
            btnBack.isVisible = true
            similarCaption.isVisible = true
        }
    }

    override fun openVacancyFragment(vacancy: Vacancy) {
        findNavController().navigate(
            R.id.action_similarVacancyFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.id, VacancyFragment.SEARCH_FRAGMENT_ORIGIN)
        )
    }

    companion object {
        private const val ARGS_SIMILAR_VACANCY_ID = "ARGS_SIMILAR_VACANCY_ID"

        fun createArgs(vacancyId: String): Bundle = bundleOf(
            ARGS_SIMILAR_VACANCY_ID to vacancyId,
        )
    }
}
