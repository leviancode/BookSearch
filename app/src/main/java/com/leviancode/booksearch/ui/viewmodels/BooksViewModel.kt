package com.leviancode.booksearch.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.leviancode.booksearch.core.models.Book
import com.leviancode.booksearch.core.models.PrintType
import com.leviancode.booksearch.core.models.Query
import com.leviancode.booksearch.core.models.SearchFilter
import com.leviancode.booksearch.core.models.json.Item
import com.leviancode.booksearch.core.repo.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.Exception

class BooksViewModel : ViewModel(){
    private val repository = BooksRepository
    private val query = Query
    private val _data = MutableLiveData<PagingData<Book>?>()
    val data: LiveData<PagingData<Book>?> = _data

    fun search(keyword: String) {
        search(query.apply {
            this.keyword = keyword
        })
    }

  /*  fun search(keyword: String) = repository.find(query.apply {
        this.keyword = keyword
    }).map { it.map { it.toBook() } }
        .cachedIn(viewModelScope)
*/
    private fun search(query: Query){
        if (query.keyword.isBlank()){
            _data.value = null
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                repository.find(query)
                    .map { it.map { it.toBook() } }
                    .cachedIn(viewModelScope)
                    //.distinctUntilChanged()
                    .collect {
                        _data.postValue(it)
                    }
            }
        }
    }

    fun setFilter(filter: String){
        query.filter = try {
            SearchFilter.valueOf(filter)
        } catch (e: Exception){
            SearchFilter.ALL
        }
        search(query)
    }

    fun setPrintType(type: PrintType){
        query.printType = type
        search(query)
    }

    fun setMaxResult(max: Int){
        query.resultCount = if (max <= 40) max else 40
        search(query)
    }

    fun getFilter(): String = query.filter.name


    private fun Item.toBook(): Book{
        return Book(
            id = this.id,
            title = volumeInfo.title,
            author = volumeInfo.authors.joinToString(", "),
            publisher = volumeInfo.publisher,
            description = volumeInfo.description,
            image = volumeInfo.imageLinks.thumbnail.replace("http", "https")
        )
    }

}