package com.leviancode.booksearch.core.models

object Query{
    var keyword: String = ""
    var filter: SearchFilter = SearchFilter.ALL
    var printType: PrintType = PrintType.ALL

    fun getQueryWithFilter() = "$keyword${filter.parameter}"
}
