package com.leviancode.booksearch.core.models.json

data class BookListResponse(
    var items: List<Item> = listOf(),
    var kind: String = "",
    var totalItems: Int = 0
)