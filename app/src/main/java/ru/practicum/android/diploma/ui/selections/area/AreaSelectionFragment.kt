package ru.practicum.android.diploma.ui.selections.area

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentAreaSelectionBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.presentation.selections.area.AreaSelectionViewModel
import ru.practicum.android.diploma.ui.fragment.BindingFragment

class AreaSelectionFragment : BindingFragment<FragmentAreaSelectionBinding>() {

    private val viewModel by viewModel<AreaSelectionViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAreaSelectionBinding {
        return FragmentAreaSelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        viewModel.getSelectedCountry().observe(viewLifecycleOwner) { country ->
            country?.let {
                setSelectedCountry(country = it)
            }
        }
    }

    private fun initUI() {
        initCountryLayout()
    }

    private fun initCountryLayout() {
        viewModel.getCountry()
        binding.countryLayout.setOnClickListener {
            findNavController().navigate(R.id.action_areaSelectionFragment_to_countrySelectionFragment)
        }
    }

    private fun setSelectedCountry(country: Country) {
        binding.countryNameTitle.visibility = View.GONE
        binding.countrySupportText.visibility = View.VISIBLE
        binding.countryName.visibility = View.VISIBLE
        binding.countryName.text = country.name
    }
}
