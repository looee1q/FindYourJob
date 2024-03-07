package ru.practicum.android.diploma.ui.selections.region

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
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

        val countryId = requireArguments().getString(ARGS_COUNTRY_ID, "").toString()
        if (countryId.isEmpty()) {
            viewModel.getRegions()
        } else {
            viewModel.getRegionsOfCountry(countryId)
        }

        binding.inputEditText.doOnTextChanged { text, start, before, count ->
            regionAdapter?.filter(text.toString())

            if (text.isNullOrBlank()) {
                binding.icClose.setImageResource(R.drawable.ic_search)
                binding.icClose.isClickable = false
            } else {
                binding.icClose.setImageResource(R.drawable.ic_close)
                binding.icClose.isClickable = true
            }
        }

        binding.icClose.setOnClickListener {
            binding.inputEditText.setText("")
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
        setFragmentResult(
            REQUEST_KEY_REGION,
            bundleOf(
                "ID" to region.id,
                "NAME" to region.name
            )
        )
        findNavController().navigateUp()

    }

    private fun render(state: RegionSelectionState) {
        when (state) {
            is RegionSelectionState.Content -> {
                renderContent(state.regions)
            }

            RegionSelectionState.Empty -> {
                renderEmptiness()
            }

            RegionSelectionState.Error -> {
                renderError()
            }

            RegionSelectionState.NoInternet -> {
                renderNoInternet()
            }

            RegionSelectionState.Loading -> {
                renderLoading()
            }
        }
    }

    private fun renderContent(regions: List<Region>) {
        binding.regionRecyclerView.isVisible = true
        binding.llErrorPlaceholder.isVisible = false
        binding.progressBar.isVisible = false
        regionAdapter?.setItems(regions)
    }

    private fun renderEmptiness() {
        binding.regionRecyclerView.isVisible = false
        binding.llErrorPlaceholder.isVisible = true
        binding.progressBar.isVisible = false
        binding.imageError.setImageResource(R.drawable.png_nothing_found)
        binding.textError.setText(R.string.nothing_found_regions)
    }

    private fun renderError() {
        binding.regionRecyclerView.isVisible = false
        binding.llErrorPlaceholder.isVisible = true
        binding.progressBar.isVisible = false
        binding.imageError.setImageResource(R.drawable.png_no_regions)
        binding.textError.setText(R.string.failed_to_retrieve_list)
    }

    private fun renderNoInternet() {
        binding.regionRecyclerView.isVisible = false
        binding.llErrorPlaceholder.isVisible = true
        binding.progressBar.isVisible = false
        binding.imageError.setImageResource(R.drawable.png_no_internet)
        binding.textError.setText(R.string.no_internet)
    }

    private fun renderLoading() {
        binding.regionRecyclerView.isVisible = false
        binding.llErrorPlaceholder.isVisible = false
        binding.progressBar.isVisible = true
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 0L
        private const val ARGS_COUNTRY_ID = "ARGS_COUNTRY_ID"
        const val REQUEST_KEY_REGION = "REQUEST_KEY_REGION"

        fun createArgs(countryId: String): Bundle = bundleOf(
            ARGS_COUNTRY_ID to countryId,
        )
    }
}
