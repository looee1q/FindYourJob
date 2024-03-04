package ru.practicum.android.diploma.ui.filter

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.ui.fragment.BindingFragment

class FilterSettingsFragment : BindingFragment<FragmentFilterSettingsBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterSettingsBinding {
        return FragmentFilterSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Аналог данных, хранящихся в LiveData во ViewModel. Удалить после внедрения ViewModel
        renderInitialState(InitialState(true, true, true, true))

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.industryField.root.setOnClickListener {
            findNavController().navigate(
                R.id.action_filterSettingsFragment_to_industrySelectionFragment
            )
        }

        binding.areaField.root.setOnClickListener {
            findNavController().navigate(
                R.id.action_filterSettingsFragment_to_areaSelectionFragment
            )
        }

        binding.areaField.arrowForwardButton.setOnClickListener {
            if (binding.areaField.arrowForwardButton.isSelected) {
                returnAreaFieldToDefaultState()
            }
        }

        binding.industryField.arrowForwardButton.setOnClickListener {
            if (binding.industryField.arrowForwardButton.isSelected) {
                returnIndustryFieldToDefaultState()
            }
        }

        binding.resetButton.setOnClickListener {
            returnFiltersToDefaultState()
        }

        binding.inputEditText.doOnTextChanged { text, start, before, count ->
            if (text?.isDigitsOnly() != true) {
                // Для предотвращения ввода каких-либо символов помимо цифр
            }
        }
    }

    private fun renderInitialState(initialState: InitialState) {
        if (initialState.isAreaChosen) {
            setCustomStateToAreaField()
        } else {
            returnAreaFieldToDefaultState()
        }

        if (initialState.isIndustryChosen) {
            setCustomStateToIndustryField()
        } else {
            returnIndustryFieldToDefaultState()
        }

        if (initialState.isSalaryChosen) {
            setCustomStateToSalaryField()
        } else {
            returnSalaryFieldToDefaultState()
        }

        if (initialState.doNotShowWithoutSalary) {
            binding.checkBox.isChecked = true
        } else {
            returnSalaryCheckboxToDefaultState()
        }

        binding.resetButton.isVisible = !checkIfTheStateIsDefault(initialState)
        binding.applyButton.isVisible = false
    }

    private fun setCustomStateToAreaField() {
        binding.areaField.bigTitleTextView.isVisible = false
        binding.areaField.smallTitleTextView.isVisible = true
        binding.areaField.smallTitleTextView.text = getString(R.string.area)
        binding.areaField.smallValueTextView.isVisible = true
        binding.areaField.smallValueTextView.text = "Россия, Москвабад"
        binding.areaField.arrowForwardButton.isSelected = true
    }

    private fun returnAreaFieldToDefaultState() {
        binding.areaField.bigTitleTextView.isVisible = true
        binding.areaField.bigTitleTextView.text = getString(R.string.area)
        binding.areaField.smallTitleTextView.isVisible = false
        binding.areaField.smallValueTextView.isVisible = false
        binding.areaField.arrowForwardButton.isSelected = false
    }

    private fun setCustomStateToIndustryField() {
        binding.industryField.bigTitleTextView.isVisible = false
        binding.industryField.smallTitleTextView.isVisible = true
        binding.industryField.smallTitleTextView.text = getString(R.string.industry)
        binding.industryField.smallValueTextView.isVisible = true
        binding.industryField.smallValueTextView.text = "IT"
        binding.industryField.arrowForwardButton.isSelected = true
    }

    private fun returnIndustryFieldToDefaultState() {
        binding.industryField.bigTitleTextView.isVisible = true
        binding.industryField.bigTitleTextView.text = getString(R.string.industry)
        binding.industryField.smallTitleTextView.isVisible = false
        binding.industryField.smallValueTextView.isVisible = false
        binding.industryField.arrowForwardButton.isSelected = false
    }

    private fun setCustomStateToSalaryField() {
        val statesForFloatingHint = arrayOf(
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(-android.R.attr.state_focused),
        )

        val colorsForFloatingHint = intArrayOf(
            resources.getColor(R.color.blue, null),
            resources.getColor(R.color.black, null),
        )

        binding.textInputLayout.defaultHintTextColor = ColorStateList(statesForFloatingHint, colorsForFloatingHint)

        binding.inputEditText.setText("120000")
    }

    private fun returnSalaryFieldToDefaultState() {
        val statesForFloatingHint = arrayOf(
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(-android.R.attr.state_focused),
        )

        val colorsForFloatingHint = intArrayOf(
            resources.getColor(R.color.blue, null),
            resources.getColor(R.color.gray_day_white_night, null),
        )

        binding.textInputLayout.defaultHintTextColor = ColorStateList(statesForFloatingHint, colorsForFloatingHint)

        binding.inputEditText.setText("")
        binding.inputEditText.clearFocus()
    }

    private fun returnSalaryCheckboxToDefaultState() {
        binding.checkBox.isChecked = false
    }

    private fun returnFiltersToDefaultState() {
        returnIndustryFieldToDefaultState()
        returnAreaFieldToDefaultState()
        returnSalaryFieldToDefaultState()
        returnSalaryCheckboxToDefaultState()
        binding.applyButton.isVisible = false
        binding.resetButton.isVisible = false
    }

    private fun checkIfTheStateIsDefault(initialState: InitialState): Boolean {
        return !initialState.isIndustryChosen
            && !initialState.isAreaChosen
            && !initialState.isSalaryChosen
            && initialState.doNotShowWithoutSalary
    }

    // Класс для демонстрации работы всех элементов интерфейса (аналог LiveData из ViewModel)
    data class InitialState(
        val isIndustryChosen: Boolean = false,
        val isAreaChosen: Boolean = false,
        val isSalaryChosen: Boolean = false,
        val doNotShowWithoutSalary: Boolean = true
    )
}
