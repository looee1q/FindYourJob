package ru.practicum.android.diploma.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.ui.fragment.BindingFragment

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icClose.setOnClickListener {
            binding.InputEditText.setText("")
            hideKeyBoard()
            binding.placeHolderError.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            //adapter.track.clear()
            //adapter.notifyDataSetChanged()
        }

        binding.InputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.placeHolderError.visibility = View.GONE
                //binding.icClose.visibility = clearButtonVisibility(s)
                //viewModel.searchDebounce(changedText = s?.toString() ?: inputText, false)

                if (binding.InputEditText.hasFocus() && s?.isEmpty() == true) {
                    binding.recyclerViewFoundVacancies.visibility = View.GONE
                    binding.icClose.visibility = View.GONE
                } else {
                    binding.recyclerViewFoundVacancies.visibility = View.VISIBLE
                    binding.icClose.visibility = View.VISIBLE

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.InputEditText.windowToken, 0)
    }
}
