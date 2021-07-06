package com.leviancode.booksearch.ui.viewmodels

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
import com.leviancode.booksearch.ui.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BooksViewModel : ViewModel(){
    private val repository = BooksRepository
    private val query = Query
    private val _data = MutableLiveData<PagingData<Book>>()
    val data: LiveData<PagingData<Book>> = _data

    private val _invalidateList = SingleLiveEvent<Unit>()
    val invalidateList: LiveData<Unit> = _invalidateList

    fun search(keyword: String) {
        search(query.apply {
            this.keyword = keyword
        })
    }

    private fun search(query: Query){
        viewModelScope.launch(Dispatchers.IO) {
            repository.find(query)
                .map { it.map { it.toBook() } }
                .cachedIn(viewModelScope)
                .collect {
                    _data.postValue(it)
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

    fun setPrintType(type: String){
        query.printType = try {
            PrintType.valueOf(type)
        } catch (e: Exception){
            PrintType.ALL
        }
        search(query)
    }

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