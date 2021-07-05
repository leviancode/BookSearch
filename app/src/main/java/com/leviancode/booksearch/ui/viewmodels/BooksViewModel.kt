package com.leviancode.booksearch.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.booksearch.core.models.Book
import com.leviancode.booksearch.core.models.PrintType
import com.leviancode.booksearch.core.models.Query
import com.leviancode.booksearch.core.models.SearchFilter
import com.leviancode.booksearch.ui.utils.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class BooksViewModel(private val repository: Repository) : ViewModel(){
    private val query = Query
    private val _data = MutableLiveData<List<Book>>()
    val data: LiveData<List<Book>> = _data

    fun search(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.find(query.apply {
                this.keyword = keyword
            })
            _data.postValue(result)
        }
    }

    fun setFilter(filter: String){
        query.filter = try {
            SearchFilter.valueOf(filter)
        } catch (e: Exception){
            SearchFilter.ALL
        }
    }

    fun setPrintType(type: PrintType){
        query.printType = type
    }

    fun setMaxResult(max: Int){
        query.maxResult = if (max <= 40) max else 40
    }

    fun getFilter(): String = query.filter.name
}