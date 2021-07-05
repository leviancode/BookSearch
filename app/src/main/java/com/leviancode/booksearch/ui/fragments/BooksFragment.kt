package com.leviancode.booksearch.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.leviancode.booksearch.R
import com.leviancode.booksearch.core.repo.GoogleBooksRepository
import com.leviancode.booksearch.databinding.FragmentBooksBinding
import com.leviancode.booksearch.ui.adapters.BookListAdapter
import com.leviancode.booksearch.ui.utils.PREF_KEY_FILTER
import com.leviancode.booksearch.ui.utils.dataStore
import com.leviancode.booksearch.ui.utils.navigate
import com.leviancode.booksearch.ui.viewmodels.BooksViewModel
import com.leviancode.booksearch.ui.viewmodels.BooksViewModelFactory
import com.pawegio.kandroid.onQueryChange
import kotlinx.coroutines.flow.collect

class BooksFragment : Fragment() {
    private val viewModel: BooksViewModel by viewModels{ BooksViewModelFactory(GoogleBooksRepository) }
    private lateinit var binding: FragmentBooksBinding
    private lateinit var listAdapter: BookListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupToolbar()
        observeData()
        observeSettings()
    }

    private fun observeData() {
        viewModel.data.observe(viewLifecycleOwner){ books ->
            listAdapter.submitList(books)
            emptyListTextVisibility(books.isNullOrEmpty())
        }
    }

    private fun observeSettings() {
        lifecycleScope.launchWhenStarted {
            requireContext().dataStore.data.collect { pref ->
                val filter = pref[stringPreferencesKey(PREF_KEY_FILTER)] ?: ""
                viewModel.setFilter(filter)
            }
        }
    }

    private fun emptyListTextVisibility(visible: Boolean) {
        binding.textEmptyList.visibility = if (visible) {
            View.VISIBLE
        } else View.GONE
    }


    private fun setupToolbar() {
        val searchView: SearchView =
            binding.booksToolbar.menu.findItem(R.id.menu_search).actionView as SearchView
        searchView.onQueryChange { query ->
            viewModel.search(query)
        }
        binding.booksToolbar.menu.findItem(R.id.navigation_settings).setOnMenuItemClickListener {
            openSettings(viewModel.getFilter())
            return@setOnMenuItemClickListener true
        }
    }

    private fun setupRecyclerView() {
        listAdapter = BookListAdapter()
        binding.bookList.adapter = listAdapter
    }

    private fun openSettings(currentFilter: String) {
        navigate {
            BooksFragmentDirections.actionNavigationBooksToNavigationSettings(currentFilter)
        }
    }
}