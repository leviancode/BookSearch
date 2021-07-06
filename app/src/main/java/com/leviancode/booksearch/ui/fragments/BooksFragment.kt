package com.leviancode.booksearch.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.leviancode.booksearch.R
import com.leviancode.booksearch.core.repo.BooksRepository
import com.leviancode.booksearch.databinding.FragmentBooksBinding
import com.leviancode.booksearch.ui.adapters.BookListAdapter
import com.leviancode.booksearch.ui.utils.PREF_KEY_FILTER
import com.leviancode.booksearch.ui.utils.dataStore
import com.leviancode.booksearch.ui.utils.navigate
import com.leviancode.booksearch.ui.viewmodels.BooksViewModel
import com.pawegio.kandroid.onQueryChange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class BooksFragment : Fragment() {
    private val viewModel: BooksViewModel by viewModels()
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
        setListeners()
        observeData()
        observeSearchFilters()
    }

    private fun observeData() {
        viewModel.data.observe(viewLifecycleOwner) { book ->
            if (book != null) {
                lifecycleScope.launchWhenStarted {
                    listAdapter.submitData(book)
                }
            }
            emptyListTextVisibility(book == null)

        }
    }

    private fun observeSearchFilters() {
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

    private fun setListeners() {
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = !binding.searchView.isIconified
        }
        binding.searchView.onQueryChange { query ->
            lifecycleScope.launch {
                viewModel.search(query)
            }

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