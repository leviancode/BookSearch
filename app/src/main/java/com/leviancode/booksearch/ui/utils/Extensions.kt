package com.leviancode.booksearch.ui.utils

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

val Context.dataStore by preferencesDataStore("settings")

inline fun Fragment.navigate(action: () -> NavDirections){
    findNavController().navigate(action())
}

fun Fragment.close(){
    findNavController().navigateUp()
}