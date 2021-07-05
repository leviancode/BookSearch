package com.leviancode.booksearch.core.models.json

data class BookListResponse(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)