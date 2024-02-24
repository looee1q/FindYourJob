package ru.practicum.android.diploma.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.Vacancy
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.ui.about.AboutFragment
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.util.debounce

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private val vacancyList: ArrayList<Vacancy> = arrayListOf()
    private lateinit var onVacancyClickDebounce: (Vacancy) -> Unit
    private val adapter = VacancyAdapter(vacancyList,
        object : VacancyAdapter.VacancyClickListener {
            override fun onVacancyClick(vacancy: Vacancy) {
                onVacancyClickDebounce(vacancy)
            }
        }
    )

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onVacancyClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                openVacancyFragment(it)
            }

        binding.icClose.setOnClickListener {
            binding.InputEditText.setText("")
            hideKeyBoard()
            binding.placeHolderError.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            //adapter.clear()
            //adapter.notifyDataSetChanged()
        }

        binding.InputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.placeHolderError.visibility = View.GONE
                //viewModel.searchDebounce(changedText = s?.toString() ?: inputText, false)

                if (binding.InputEditText.hasFocus() && s?.isEmpty() == true) {
                    binding.recyclerViewFoundVacancies.visibility = View.GONE
                    binding.icClose.setImageResource(R.drawable.ic_search)
                    binding.icClose.isClickable = false
                } else {
                    binding.recyclerViewFoundVacancies.visibility = View.VISIBLE
                    binding.icClose.isClickable = true
                    binding.icClose.setImageResource(R.drawable.ic_close)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.recyclerViewFoundVacancies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewFoundVacancies.adapter = adapter
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.InputEditText.windowToken, 0)
    }

    private fun openVacancyFragment(vacancy: Vacancy) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            AboutFragment.createArgs(vacancy.id)
        )
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
