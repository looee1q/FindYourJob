package ru.practicum.android.diploma.ui.selections.area

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentAreaSelectionBinding
import ru.practicum.android.diploma.ui.fragment.BindingFragment

class AreaSelectionFragment : BindingFragment<FragmentAreaSelectionBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAreaSelectionBinding {
        return FragmentAreaSelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }
}
