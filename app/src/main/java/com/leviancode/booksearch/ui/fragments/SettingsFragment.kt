package com.leviancode.booksearch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.edit
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.leviancode.booksearch.R
import com.leviancode.booksearch.core.models.SearchFilter
import com.leviancode.booksearch.databinding.FragmentSettingsBinding
import com.leviancode.booksearch.ui.utils.PREF_KEY_FILTER
import com.leviancode.booksearch.ui.utils.close
import com.leviancode.booksearch.ui.utils.dataStore
import com.pawegio.kandroid.defaultSharedPreferences
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val args: SettingsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListView()
        setListeners()
    }

    private fun setListeners() {
        binding.settingsToolbar.setNavigationOnClickListener { close() }
        binding.searchOptionList.setOnItemClickListener { parent, view, position, id ->
            saveSelectedSearchOption(position)
            close()
        }
    }

    private fun saveSelectedSearchOption(optionIndex: Int) {
        lifecycleScope.launch {
            requireContext().dataStore.edit { pref ->
                pref[stringPreferencesKey(PREF_KEY_FILTER)] = SearchFilter.values()[optionIndex].name
            }
        }
    }

    private fun setupListView() {
       val adapter =  ArrayAdapter(
            requireContext(),
            R.layout.list_item_search_option,
            resources.getStringArray(R.array.search_options)
        )
        binding.searchOptionList.adapter = adapter
        binding.searchOptionList.setSelection(getIndexOfCurrentFilter())
    }

    private fun getIndexOfCurrentFilter() = SearchFilter.valueOf(args.currentFilter).ordinal
}