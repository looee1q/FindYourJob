package ru.practicum.android.diploma.ui.region_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentRegionSelectionBinding
import ru.practicum.android.diploma.ui.fragment.BindingFragment

class RegionSelectionFragment : BindingFragment<FragmentRegionSelectionBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegionSelectionBinding {
        return FragmentRegionSelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}
