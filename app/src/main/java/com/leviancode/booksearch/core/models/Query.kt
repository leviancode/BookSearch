package com.leviancode.booksearch.core.models

object Query{
    var keyword: String = ""
    var filter: SearchFilter = SearchFilter.ALL
    var printType: PrintType = PrintType.BOOKS
    var startIndex: Int = 0
    var maxResult: Int = 10

    fun getQueryWithFilter() =
        keyword + if (filter.parameter.isNotBlank()) "+${filter.parameter}" else ""
}
