package ru.practicum.android.diploma.ui.selections.area

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentAreaSelectionBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.presentation.selections.area.AreaSelectionViewModel
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.ui.selections.country.CountrySelectionFragment

class AreaSelectionFragment : BindingFragment<FragmentAreaSelectionBinding>() {

    private val viewModel by viewModel<AreaSelectionViewModel>()

    private var countryFromSharedPref: Country? = null
    private var countryName: String? = null
    private var countryId: String? = null
    private var isFirstFragmentOpen: Boolean = false

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAreaSelectionBinding {
        return FragmentAreaSelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val argsFragment = requireArguments().getString(ARGS_FIRST_OPEN).toString()
        if (argsFragment == "FilterSettingsFragment") {
            isFirstFragmentOpen = true
        }

        checkFragment()


        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.countryLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_areaSelectionFragment_to_countrySelectionFragment
            )
        }

        binding.regionLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_areaSelectionFragment_to_regionSelectionFragment
            )
        }

        binding.icArrowForwardCountry.setOnClickListener {
            setEmptyCountry()
        }

        binding.buttonSelect.setOnClickListener {

            viewModel.saveCountryToSharedPref(countryId, countryName)
            Log.e("AreaSelectionFragment", "countryId = $countryId countryName = $countryName")

            findNavController().navigateUp()
        }
    }

    private fun checkFragment() {
        if (isFirstFragmentOpen) {
            countryFromSharedPref = viewModel.getCountryFromSharedPref()
            countryName = countryFromSharedPref?.name
            Log.e("countryFromSharedPref", "countryId = $countryId countryName = $countryName")
            countryFromSharedPref?.let { setSelectedCountry(it.name) }
        } else {
            setFragmentResultListener(CountrySelectionFragment.REQUEST_KEY) { key, bundle ->
                countryName = bundle.getString("NAME")
                countryId = bundle.getString("ID")
                countryName?.let { setSelectedCountry(it) }
            }
        }
    }

    private fun setSelectedCountry(countryName: String) {
        binding.countryNameTitle.visibility = View.GONE
        binding.countrySupportText.visibility = View.VISIBLE
        binding.icArrowForwardCountry.isSelected = true
        binding.countryName.visibility = View.VISIBLE
        binding.countryName.text = countryName
        showButtonSelect()
    }

    private fun setEmptyCountry() {
        binding.countryNameTitle.visibility = View.VISIBLE
        binding.countrySupportText.visibility = View.GONE
        binding.icArrowForwardCountry.isSelected = false
        binding.countryName.visibility = View.GONE
        binding.countryName.text = ""
        showButtonSelect()
        countryName = null
        countryId = null
    }

    private fun showButtonSelect() {
        if (countryName.isNullOrEmpty()) {
            binding.buttonSelect.visibility = View.GONE
        } else {
            binding.buttonSelect.visibility = View.VISIBLE
        }
    }

    companion object {
        private var ARGS_FIRST_OPEN = ""

        fun createArgs(isFirstFragmentOpen: String): Bundle = bundleOf(
            ARGS_FIRST_OPEN to isFirstFragmentOpen
        )

    }

}
