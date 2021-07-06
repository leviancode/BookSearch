package com.leviancode.booksearch.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.leviancode.booksearch.R
import com.leviancode.booksearch.databinding.FragmentBooksBinding
import com.leviancode.booksearch.ui.adapters.BookListAdapter
import com.leviancode.booksearch.ui.utils.*
import com.leviancode.booksearch.ui.viewmodels.BooksViewModel
import com.pawegio.kandroid.onQueryChange
import kotlinx.coroutines.flow.collect
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
        observeSettings()
    }

    private fun observeData() {
        viewModel.data.observe(viewLifecycleOwner) { book ->
            lifecycleScope.launchWhenStarted {
                listAdapter.submitData(book)
            }
        }
        viewModel.invalidateList.observe(viewLifecycleOwner){
            listAdapter.submitData(lifecycle, PagingData.empty())
            listAdapter.notifyDataSetChanged()
        }
    }

    private fun observeSettings() {
        lifecycleScope.launchWhenStarted {
            requireContext().dataStore.data.collect { pref ->
                val filter = pref[stringPreferencesKey(PREF_KEY_FILTER)] ?: ""
                viewModel.setFilter(filter)
            }
        }
        lifecycleScope.launchWhenStarted {
            requireContext().dataStore.data.collect { pref ->
                val printType = pref[stringPreferencesKey(PREF_KEY_PRINT_TYPE)] ?: ""
                viewModel.setPrintType(printType)
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
                if (checkInternetConnection()){
                    emptyListTextVisibility(query.isBlank())
                }
                viewModel.search(query)
            }

        }
        binding.booksToolbar.menu.findItem(R.id.navigation_settings).setOnMenuItemClickListener {
            openSettings()
            return@setOnMenuItemClickListener true
        }
    }

    private fun checkInternetConnection(): Boolean {
        return ConnectionManager.isInternetAvailable(requireContext()).also { ok ->
            if (!ok){
                Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        listAdapter = BookListAdapter()
        binding.bookList.adapter = listAdapter
    }

    private fun openSettings() {
        navigate {
            BooksFragmentDirections.actionNavigationBooksToNavigationSettings()
        }
    }
}