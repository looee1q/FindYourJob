package ru.practicum.android.diploma.ui.selections.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
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

    }

    private fun renderCountrySelectionState(state: CountrySelectionState) {
        when (state) {
            is CountrySelectionState.Content -> {
                showCountryRecyclerView()
                hideLLErrorServer()
                countryAdapter?.let {
                    it.countries.clear()
                    it.countries.addAll(state.countries)
                    it.notifyDataSetChanged()
                }
            }

            is CountrySelectionState.Empty -> {
                hideCountryRecyclerView()
                showLLErrorServer()
            }
        }
    }

    private fun showCountryRecyclerView() {
        binding.countryRecyclerView.visibility = View.VISIBLE
    }

    private fun hideCountryRecyclerView() {
        binding.countryRecyclerView.visibility = View.GONE
    }

    private fun showLLErrorServer() {
        binding.llErrorServer.visibility = View.VISIBLE
    }

    private fun hideLLErrorServer() {
        binding.llErrorServer.visibility = View.GONE
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
