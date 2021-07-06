package com.leviancode.booksearch.core.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.leviancode.booksearch.core.models.Book
import com.leviancode.booksearch.core.models.Query
import com.leviancode.booksearch.core.models.json.Item
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BooksRepository {
    private const val BOOKS_API_BASE_URL = "https://www.googleapis.com/books/v1/"
    private const val RESULT_COUNT = 20

    private val booksService: BooksService = Retrofit.Builder()
        .baseUrl(BOOKS_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BooksService::class.java)

    fun find(query: Query): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = RESULT_COUNT,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BooksPagingSource(query, service = booksService)
            }
        ).flow
    }


}