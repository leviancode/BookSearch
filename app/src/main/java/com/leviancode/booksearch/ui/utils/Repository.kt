package com.leviancode.booksearch.ui.utils

import com.leviancode.booksearch.core.models.Book
import com.leviancode.booksearch.core.models.Query
import com.leviancode.booksearch.core.models.SearchFilter

interface Repository {
    suspend fun find(query: Query): List<Book>
}