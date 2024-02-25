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
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.search.state.SearchFragmentScreenState
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.debounce

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private val viewModel by viewModel<SearchViewModel>()
    private val vacancyList: ArrayList<Vacancy> = arrayListOf()

    private var adapter: VacancyAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onVacancyClickDebounce =
            debounce<Vacancy>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { openVacancyFragment(it) }

        val start = SearchFragmentScreenState.Start
        render(start)

        binding.InputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.placeHolderError.visibility = View.GONE
                viewModel.search(binding.InputEditText.text.toString())
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

            override fun afterTextChanged(s: Editable?) {}
        })

        adapter = VacancyAdapter(vacancyList, object : VacancyAdapter.VacancyClickListener {
            override fun onVacancyClick(vacancy: Vacancy) {
                onVacancyClickDebounce(vacancy)
            }
        })

        binding.recyclerViewFoundVacancies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewFoundVacancies.adapter = adapter

        binding.icClose.setOnClickListener {
            binding.InputEditText.setText("")
            hideKeyBoard()
            binding.placeHolderError.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.messageFound.visibility = View.GONE
            adapter?.let {
                vacancyList.clear()
                it.notifyDataSetChanged()
            }
        }
        viewModel.getSearchFragmentScreenState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: SearchFragmentScreenState) {
        when (state) {
            is SearchFragmentScreenState.Content -> showContent(state.vacancies)
            is SearchFragmentScreenState.Empty -> showEmpty()
            is SearchFragmentScreenState.Error -> showError()
            is SearchFragmentScreenState.Loading -> showLoading()
            is SearchFragmentScreenState.Start -> showStart()
        }
    }

    private fun showStart() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerViewFoundVacancies.visibility = View.GONE
        binding.ImageSearch.visibility = View.VISIBLE
        binding.messageFound.visibility = View.GONE
        binding.placeHolderError.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewFoundVacancies.visibility = View.GONE
        binding.ImageSearch.visibility = View.GONE
        binding.messageFound.visibility = View.GONE
        binding.placeHolderError.visibility = View.GONE
    }

    private fun showError() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerViewFoundVacancies.visibility = View.GONE
        binding.ImageSearch.visibility = View.GONE
        binding.messageFound.visibility = View.GONE
        binding.placeHolderError.visibility = View.VISIBLE
        binding.messageError.text = getString(R.string.no_internet)
        binding.imageError.setImageResource(R.drawable.png_no_internet)
        hideKeyBoard()
    }

    private fun showEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerViewFoundVacancies.visibility = View.GONE
        binding.ImageSearch.visibility = View.GONE
        binding.messageFound.visibility = View.VISIBLE
        binding.messageFound.text = getString(R.string.nothing_found)
        binding.placeHolderError.visibility = View.VISIBLE
        binding.messageError.text = getString(R.string.nothing_found_description)
        binding.imageError.setImageResource(R.drawable.png_nothing_found)
        hideKeyBoard()
    }

    private fun showContent(vacancies: List<Vacancy>) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerViewFoundVacancies.visibility = View.GONE
        binding.ImageSearch.visibility = View.GONE
        binding.placeHolderError.visibility = View.GONE
        binding.messageFound.visibility = View.VISIBLE
        binding.messageFound.text = getString(R.string.count_found_vacancies, vacancies.size.toString())
        binding.recyclerViewFoundVacancies.visibility = View.VISIBLE
        hideKeyBoard()
        adapter?.let {
            vacancyList.clear()
            vacancyList.addAll(vacancies)
            it.notifyDataSetChanged()
        }
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.InputEditText.windowToken, 0)
    }

    private fun openVacancyFragment(vacancy: Vacancy) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.id)
        )
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 0L
    }
}
