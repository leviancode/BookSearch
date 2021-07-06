package com.leviancode.booksearch.core.models

enum class SearchFilter(val parameter: String) {
    ALL(""),
    TITLE("+intitle"),
    AUTHOR("+inauthor"),
    PUBLISHER("+inpublisher")
}