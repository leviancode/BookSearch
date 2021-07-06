package com.leviancode.booksearch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.leviancode.booksearch.R
import com.leviancode.booksearch.core.models.PrintType
import com.leviancode.booksearch.core.models.SearchFilter
import com.leviancode.booksearch.databinding.FragmentSettingsBinding
import com.leviancode.booksearch.ui.utils.close
import com.leviancode.booksearch.ui.viewmodels.SettingsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

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
        setupOptionsListView()
        setupPrintTypesListView()
        setListeners()
    }

    private fun setListeners() {
        binding.settingsToolbar.setNavigationOnClickListener { close() }
        binding.searchOptionList.setOnItemClickListener { parent, view, position, id ->
            lifecycleScope.launch {
                viewModel.saveSelectedSearchFilter(requireContext(), position)
            }.invokeOnCompletion {
                close()
            }
        }
        binding.searchPrintTypeList.setOnItemClickListener { parent, view, position, id ->
            lifecycleScope.launch {
                viewModel.saveSelectedPrintType(requireContext(), position)
            }.invokeOnCompletion {
                close()
            }
        }
    }

    private fun setupOptionsListView() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_single_choice,
            resources.getStringArray(R.array.search_options)
        )
        binding.searchOptionList.adapter = adapter

        lifecycleScope.launch {
            viewModel.getCurrentSearchFilter(requireContext()).collect {
                binding.searchOptionList.setItemChecked(getIndexOfCurrentOptionFilter(it), true)
            }
        }
    }

    private fun setupPrintTypesListView() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_single_choice,
            resources.getStringArray(R.array.search_options_pup_type)
        )
        binding.searchPrintTypeList.adapter = adapter
        lifecycleScope.launch {
            viewModel.getCurrentPrintType(requireContext()).collect {
                binding.searchPrintTypeList.setItemChecked(
                    getIndexOfCurrentPrintTypeFilter(it),
                    true
                )
            }
        }
    }

    private fun getIndexOfCurrentOptionFilter(filter: String?) = try {
        if (!filter.isNullOrBlank()) SearchFilter.valueOf(filter).ordinal
        else 0
    } catch (e: Exception) {
        0
    }

    private fun getIndexOfCurrentPrintTypeFilter(type: String?) = try {
        if (!type.isNullOrBlank()) PrintType.valueOf(type).ordinal
        else 0
    } catch (e: Exception) {
        0
    }
}