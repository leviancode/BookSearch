package com.leviancode.booksearch.core.models

object Query{
    var keyword: String = ""
    var filter: SearchFilter = SearchFilter.ALL
    var printType: PrintType = PrintType.BOOKS
    var startIndex: Int = 0
    var resultCount: Int = 20

    fun getQueryWithFilter() = "$keyword${filter.parameter}"
}
