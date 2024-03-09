package ru.practicum.android.diploma.ui.selections.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentCountrySelectionBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.presentation.selections.country.CountrySelectionViewModel
import ru.practicum.android.diploma.presentation.selections.country.state.CountrySelectionState
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.ui.selections.country.adapter.CountryAdapter
import ru.practicum.android.diploma.util.debounce

class CountrySelectionFragment : BindingFragment<FragmentCountrySelectionBinding>() {

    private val viewModel by viewModel<CountrySelectionViewModel>()

    private var countryAdapter: CountryAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCountrySelectionBinding {
        return FragmentCountrySelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getCountries()
        viewModel.getCountrySelectionState().observe(viewLifecycleOwner) {
            renderCountrySelectionState(it)
        }
    }

    private fun initUi() {
        setupBackButton()
        initBtnBack()
        initCountryRecyclerView()
    }

    private fun setupBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackButtonClick()
            }
        })
    }

    private fun initBtnBack() {
        binding.btnBack.setOnClickListener {
            onBackButtonClick()
        }
    }

    private fun initCountryRecyclerView() {
        val onCountryClickDebounce: (Country) -> Unit =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                onCountryClick(it)
            }
        countryAdapter = CountryAdapter(onCountryClickDebounce)
        binding.countryRecyclerView.adapter = countryAdapter
        binding.countryRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun onBackButtonClick() {
        requireActivity().supportFragmentManager.popBackStackImmediate()
    }

    private fun onCountryClick(country: Country) {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(
                "ID" to country.id,
                "NAME" to country.name
            )
        )
        onBackButtonClick()
    }

    private fun renderCountrySelectionState(state: CountrySelectionState) {
        when (state) {
            is CountrySelectionState.Content -> {
                showCountryRecyclerView()
                hideLoader()
                hideLLErrorServer()
                countryAdapter?.let {
                    it.countries.clear()
                    it.countries.addAll(state.countries)
                    it.notifyDataSetChanged()
                }

            }

            is CountrySelectionState.Empty -> {
                hideCountryRecyclerView()
                hideLoader()
                showLLErrorServer(
                    imageResource = R.drawable.empty_favorites,
                    titleResource = R.string.countries_are_empty
                )
            }

            is CountrySelectionState.Error -> {
                hideCountryRecyclerView()
                hideLoader()
                showLLErrorServer(
                    imageResource = R.drawable.png_no_regions,
                    titleResource = R.string.failed_to_retrieve_list
                )
            }

            is CountrySelectionState.Loading -> {
                showLoader()
                hideCountryRecyclerView()
                hideLLErrorServer()
            }

            is CountrySelectionState.NoInternet -> {
                hideCountryRecyclerView()
                hideLoader()
                showLLErrorServer(
                    imageResource = R.drawable.png_no_internet,
                    titleResource = R.string.no_internet
                )
            }
        }
    }

    private fun showCountryRecyclerView() {
        binding.countryRecyclerView.visibility = View.VISIBLE
    }

    private fun hideCountryRecyclerView() {
        binding.countryRecyclerView.visibility = View.GONE
    }

    private fun showLLErrorServer(@DrawableRes imageResource: Int, @StringRes titleResource: Int) {
        binding.llErrorServer.visibility = View.VISIBLE
        binding.placeholderImageView.setBackgroundResource(imageResource)
        binding.placeholderTextView.setText(titleResource)
    }

    private fun hideLLErrorServer() {
        binding.llErrorServer.visibility = View.GONE
    }

    private fun showLoader() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.progressBar.visibility = View.GONE
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val REQUEST_KEY = "REQUEST_KEY"
    }
}
