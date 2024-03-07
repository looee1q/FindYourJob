package ru.practicum.android.diploma.ui.selections.area

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentAreaSelectionBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.presentation.selections.area.AreaSelectionViewModel
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.ui.selections.country.CountrySelectionFragment
import ru.practicum.android.diploma.ui.selections.region.RegionSelectionFragment

class AreaSelectionFragment : BindingFragment<FragmentAreaSelectionBinding>() {

    private val viewModel by viewModel<AreaSelectionViewModel>()

    private var countryFromSharedPref: Country? = null
    private var countryNameFromSharedPref: String = ""
    private var countryIdFromSharedPref: String = ""
    private var countryName: String = ""
    private var countryId: String = ""

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAreaSelectionBinding {
        return FragmentAreaSelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countryFromSharedPref = viewModel.getCountryFromSharedPref()
        countryNameFromSharedPref = countryFromSharedPref?.name ?: ""

        setFragmentResultListener(CountrySelectionFragment.REQUEST_KEY) { key, bundle ->
            countryName = bundle.getString("NAME") ?: ""
            countryId = bundle.getString("ID") ?: ""
            checkFragment()
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
                R.id.action_areaSelectionFragment_to_regionSelectionFragment,
                RegionSelectionFragment.createArgs("40")
            )
        }

        binding.icArrowForwardCountry.setOnClickListener {
            setEmptyCountry()
            viewModel.saveCountryToSharedPref(countryId, countryName)
        }

        binding.buttonSelect.setOnClickListener {
            viewModel.saveCountryToSharedPref(countryId, countryName)
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )
    }

    private fun checkFragment() {
        if (countryNameFromSharedPref.isNotEmpty() && countryName.isEmpty()) {
            countryFromSharedPref?.let { setSelectedCountry(it.name) }
        } else if (countryNameFromSharedPref.isEmpty() && countryName.isEmpty()) {
            setEmptyCountry()
        } else if (countryName.isNotEmpty()) {
            setSelectedCountry(countryName)
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
        countryName = ""
        countryId = ""
        showButtonSelect()
    }

    private fun showButtonSelect() {
        if (countryName.isEmpty() && countryNameFromSharedPref.isEmpty()) {
            binding.buttonSelect.visibility = View.GONE
        } else {
            binding.buttonSelect.visibility = View.VISIBLE
        }
    }

}
