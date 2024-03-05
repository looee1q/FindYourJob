package ru.practicum.android.diploma.ui.selections.region

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentRegionSelectionBinding
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.selections.region.RegionSelectionViewModel
import ru.practicum.android.diploma.presentation.selections.region.state.RegionSelectionState
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.util.debounce

class RegionSelectionFragment : BindingFragment<FragmentRegionSelectionBinding>() {

    private val viewModel by viewModel<RegionSelectionViewModel>()

    private var regionAdapter: RegionAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegionSelectionBinding {
        return FragmentRegionSelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        initRegionRecyclerView()

        if (binding.InputEditText.text.isEmpty()) {
            viewModel.getRegions()
        }

        viewModel.regionsStateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

    }

    private fun initRegionRecyclerView() {
        val onClickDebounce: (Region) -> Unit =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                onRegionClick(it)
            }
        regionAdapter = RegionAdapter(onClickDebounce)
        binding.regionRecyclerView.adapter = regionAdapter
        binding.regionRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun onRegionClick(region: Region) {
        println()
    }

    private fun render(state: RegionSelectionState) {
        when (state) {
            is RegionSelectionState.Content -> {
                regionAdapter?.countries?.addAll(
                    state.regions
                )
            }
            RegionSelectionState.Empty -> {

            }
            RegionSelectionState.Error -> {

            }
            RegionSelectionState.Loading -> {

            }
            RegionSelectionState.NoInternet -> {

            }
        }
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
