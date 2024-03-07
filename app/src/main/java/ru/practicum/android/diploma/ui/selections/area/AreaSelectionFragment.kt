package ru.practicum.android.diploma.ui.selections.area

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentAreaSelectionBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.selections.area.AreaSelectionViewModel
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.ui.selections.country.CountrySelectionFragment
import ru.practicum.android.diploma.ui.selections.region.RegionSelectionFragment

class AreaSelectionFragment : BindingFragment<FragmentAreaSelectionBinding>() {

    private val viewModel by viewModel<AreaSelectionViewModel>()

    private var countryFromSharedPref: Country? = null
    private var countryNameFromSharedPref: String = ""
    private var countryIdFromSharedPref: String = ""
    private var regionFromSharedPref: Region? = null
    private var regionNameFromSharedPref: String = ""
    private var regionIdFromSharedPref: String = ""
    private var countryName: String = ""
    private var countryId: String = ""
    private var regionName: String = ""
    private var regionId: String = ""

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAreaSelectionBinding {
        return FragmentAreaSelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        installFromSharedPref()
        checkCountry()
        checkRegion()

        setFragmentResultListener(CountrySelectionFragment.REQUEST_KEY) { key, bundle ->
            countryName = bundle.getString("NAME") ?: ""
            countryId = bundle.getString("ID") ?: ""
            checkCountry()
        }

        setFragmentResultListener(RegionSelectionFragment.REQUEST_KEY_REGION) { key, bundle ->
            regionName = bundle.getString("NAME") ?: ""
            regionId = bundle.getString("ID") ?: ""
            checkRegion()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.countryLayout.setOnClickListener {
            findNavController().navigate(R.id.action_areaSelectionFragment_to_countrySelectionFragment)
        }

        binding.regionLayout.setOnClickListener {
            openRegionSelectionFragment()
        }

        binding.icArrowForwardCountry.setOnClickListener {
            clearCountry()
        }

        binding.icArrowForwardRegion.setOnClickListener {
            clearRegion()
        }

        binding.buttonSelect.setOnClickListener {
            saveCountryAndRegion()
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

    private fun installFromSharedPref() {
        countryFromSharedPref = viewModel.getCountryFromSharedPref()
        countryNameFromSharedPref = countryFromSharedPref?.name ?: ""
        countryIdFromSharedPref = countryFromSharedPref?.id ?: ""
        regionFromSharedPref = viewModel.getRegionFromSharedPref()
        regionNameFromSharedPref = regionFromSharedPref?.name ?: ""
        regionIdFromSharedPref = regionFromSharedPref?.id ?: ""
    }

    private fun checkCountry() {
        if (countryNameFromSharedPref.isNotEmpty() && countryName.isEmpty()) {
            countryFromSharedPref?.let { setSelectedCountry(it.name) }
        } else if (countryNameFromSharedPref.isEmpty() && countryName.isEmpty()) {
            setEmptyCountry()
        } else if (countryName.isNotEmpty()) {
            setSelectedCountry(countryName)
        }
    }

    private fun checkRegion() {
        if (regionNameFromSharedPref.isNotEmpty() && regionName.isEmpty()) {
            regionFromSharedPref?.let { setSelectedRegion(it.name) }
        } else if (regionNameFromSharedPref.isEmpty() && regionName.isEmpty()) {
            setEmptyRegion()
        } else if (regionName.isNotEmpty()) {
            setSelectedRegion(regionName)
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

    private fun setSelectedRegion(countryName: String) {
        binding.RegionNameTitle.visibility = View.GONE
        binding.RegionSupportText.visibility = View.VISIBLE
        binding.icArrowForwardRegion.isSelected = true
        binding.RegionName.visibility = View.VISIBLE
        binding.RegionName.text = countryName
        showButtonSelect()
    }

    private fun setEmptyRegion() {
        binding.RegionNameTitle.visibility = View.VISIBLE
        binding.RegionSupportText.visibility = View.GONE
        binding.icArrowForwardRegion.isSelected = false
        binding.RegionName.visibility = View.GONE
        binding.RegionName.text = ""
        regionName = ""
        regionId = ""
        showButtonSelect()
    }

    private fun clearRegion() {
        setEmptyRegion()
        viewModel.saveRegionToSharedPref(regionId, regionName)
    }

    private fun clearCountry() {
        setEmptyCountry()
        viewModel.saveCountryToSharedPref(countryId, countryName)
    }

    private fun showButtonSelect() {
        if (binding.RegionNameTitle.isVisible && binding.countryNameTitle.isVisible) {
            binding.buttonSelect.visibility = View.GONE
        } else {
            binding.buttonSelect.visibility = View.VISIBLE
        }
    }

    private fun openRegionSelectionFragment() {
        findNavController().navigate(
            R.id.action_areaSelectionFragment_to_regionSelectionFragment,
            if (countryId.isNotEmpty()) {
                RegionSelectionFragment.createArgs(countryId)
            } else {
                RegionSelectionFragment.createArgs(countryIdFromSharedPref)
            }
        )
    }

    private fun saveCountryAndRegion() {
        if (countryId.isNotEmpty()) {
            viewModel.saveCountryAndRegionToSharedPref(countryId, countryName, regionId, regionName)
        } else if (countryId.isEmpty() && regionId.isNotEmpty()) {
            viewModel.saveCountryAndRegionToSharedPref(
                countryIdFromSharedPref,
                countryNameFromSharedPref,
                regionId,
                regionName
            )
        } else {
            viewModel.saveCountryAndRegionToSharedPref(
                countryIdFromSharedPref,
                countryNameFromSharedPref,
                regionIdFromSharedPref,
                regionNameFromSharedPref
            )
        }
        findNavController().navigateUp()
    }

}
