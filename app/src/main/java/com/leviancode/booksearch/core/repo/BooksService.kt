package com.leviancode.booksearch.core.repo

import com.leviancode.booksearch.core.models.json.BookListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksService {

    @GET("volumes")
    fun getBooks(
        @Query("q") query: String,
        @Query("printType") printType: String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int,
        @Query("key") apiKey: String
    ): Call<BookListResponse>
}