package ru.practicum.android.diploma.ui.selections.industry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentIndustrySelectionBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.selections.industry.IndustrySelectionViewModel
import ru.practicum.android.diploma.presentation.selections.industry.state.IndustrySelectionState
import ru.practicum.android.diploma.ui.fragment.BindingFragment

class IndustrySelectionFragment : BindingFragment<FragmentIndustrySelectionBinding>() {

    private val viewModel by viewModel<IndustrySelectionViewModel>()
    private val adapter = IndustryAdapter(
        object : IndustryAdapter.IndustryClickListener {
            override fun onIndustryClick(industry: Industry, position: Int) {
                viewModel.saveCheckedIndustry(industry, position)
            }
        }
    )

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentIndustrySelectionBinding {
        return FragmentIndustrySelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonConfirm.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "${viewModel.checkedIndustry?.id}  ${viewModel.checkedIndustry?.name}",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.rvIndustries.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvIndustries.adapter = adapter

        viewModel.getIndustriesFragmentState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: IndustrySelectionState) {
        when (state) {
            is IndustrySelectionState.NoInternet -> {
                showNoInternet()
            }

            is IndustrySelectionState.Error -> {
                showError()
            }

            is IndustrySelectionState.Content -> {
                showContent(state.industries)
            }

            is IndustrySelectionState.ChangeCheckedIndustry -> {
                showChangeChecked(state.industries, state.oldPos, state.newPos)
            }

            is IndustrySelectionState.Loading -> {
                showLoading()
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
    }

    private fun showError() {
        binding.progressBar.isVisible = false
        binding.llErrorServer.isVisible = true
    }

    private fun showNoInternet() {
        binding.progressBar.isVisible = false
        binding.llNoInternetConnection.isVisible = true
    }

    private fun showContent(industries: List<Industry>) {
        binding.progressBar.isVisible = false
        binding.rvIndustries.isVisible = true
        adapter.setIndustryList(industries, -1, -1)
    }

    private fun showChangeChecked(industries: List<Industry>, oldChecked: Int, newChecked: Int) {
        adapter.setIndustryList(industries, oldChecked, newChecked)
        binding.buttonConfirm.isVisible = true
    }
}
