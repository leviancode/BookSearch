package com.leviancode.booksearch.core.repo

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.leviancode.booksearch.core.models.Book
import com.leviancode.booksearch.core.models.Query
import com.leviancode.booksearch.core.models.json.Item

class BooksPagingSource(val query: Query, val service: BooksService): PagingSource<Int, Item>()  {

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val pageIndex = params.key ?: 1
        return try {
            val response = service.getBooks(
                query.getQueryWithFilter(),
                query.printType.name,
                pageIndex,
                query.resultCount,
                BooksRepository.BOOKS_API_KEY
            )

            val books = response.items

            val nextKey =
                if (books.isEmpty()) {
                    null
                } else {
                    pageIndex + query.resultCount
                }
            LoadResult.Page(
                data = books,
                prevKey = if (pageIndex == 1) null else pageIndex,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}

