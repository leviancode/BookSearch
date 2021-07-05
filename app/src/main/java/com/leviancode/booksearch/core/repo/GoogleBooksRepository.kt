package com.leviancode.booksearch.core.repo

import com.leviancode.booksearch.core.models.Book
import com.leviancode.booksearch.core.models.Query
import com.leviancode.booksearch.core.models.json.BookListResponse
import com.leviancode.booksearch.ui.utils.Repository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GoogleBooksRepository : Repository {
    private const val BOOKS_API_KEY = "AIzaSyC2T40Yy4ooq958nRoBE6EAq2NsMrQPtEw"
    private const val BOOKS_API_BASE_URL = "https://www.googleapis.com/books/v1/"

    private val service: BooksService = Retrofit.Builder()
        .baseUrl(BOOKS_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BooksService::class.java)

    override suspend fun find(query: Query): List<Book> {
        val response = service.getBooks(
            query.getQueryWithFilter(),
            query.printType.name,
            query.startIndex,
            query.maxResult,
            BOOKS_API_KEY
        ).execute()

        return if (response.isSuccessful && response.body() != null) {
            simplify(response.body()!!)
        } else listOf()
    }

    private fun simplify(response: BookListResponse): List<Book>{
        return response.items.map { item ->
            Book(
                id = item.id,
                title = item.volumeInfo.title,
                author = item.volumeInfo.authors.joinToString(", "),
                publisher = item.volumeInfo.publisher,
                description = item.volumeInfo.description ?: "",
                image = item.volumeInfo.imageLinks.thumbnail
            )
        }
    }
}