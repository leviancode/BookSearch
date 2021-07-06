package com.leviancode.booksearch.core.repo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.leviancode.booksearch.core.models.Query
import com.leviancode.booksearch.core.models.json.Item

class BooksPagingSource(val query: Query, val service: BooksService): PagingSource<Int, Item>()  {
    private val BOOKS_API_KEY = "AIzaSyC2T40Yy4ooq958nRoBE6EAq2NsMrQPtEw"
    private val RESULT_COUNT = 20

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
                query = query.getQueryWithFilter(),
                printType = query.printType.name.lowercase(),
                startIndex = pageIndex,
                maxResults = RESULT_COUNT,
                apiKey = BOOKS_API_KEY
            )

            val books = response.items

            val nextKey =
                if (books.isEmpty()) {
                    null
                } else {
                    pageIndex + RESULT_COUNT
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

