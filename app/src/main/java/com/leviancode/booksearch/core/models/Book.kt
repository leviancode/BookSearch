package com.leviancode.booksearch.core.models

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val publisher: String,
    val image: String
)
