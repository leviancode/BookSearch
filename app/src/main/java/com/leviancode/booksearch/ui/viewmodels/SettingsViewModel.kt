package com.leviancode.booksearch.ui.viewmodels

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import com.leviancode.booksearch.core.models.PrintType
import com.leviancode.booksearch.core.models.SearchFilter
import com.leviancode.booksearch.ui.utils.PREF_KEY_FILTER
import com.leviancode.booksearch.ui.utils.PREF_KEY_PRINT_TYPE
import com.leviancode.booksearch.ui.utils.dataStore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class SettingsViewModel : ViewModel() {
    suspend fun saveSelectedSearchFilter(context: Context, optionIndex: Int) {
        context.dataStore.edit { pref ->
            pref[stringPreferencesKey(PREF_KEY_FILTER)] = SearchFilter.values()[optionIndex].name
        }
    }

    suspend fun saveSelectedPrintType(context: Context, optionIndex: Int) {
        context.dataStore.edit { pref ->
            pref[stringPreferencesKey(PREF_KEY_PRINT_TYPE)] = PrintType.values()[optionIndex].name
        }
    }

    fun getCurrentSearchFilter(context: Context) = flow {
        context.dataStore.data.collect { pref ->
            emit(pref[stringPreferencesKey(PREF_KEY_FILTER)])
        }
    }

    fun getCurrentPrintType(context: Context) = flow {
        context.dataStore.data.collect { pref ->
            emit(pref[stringPreferencesKey(PREF_KEY_PRINT_TYPE)])
        }
    }


}