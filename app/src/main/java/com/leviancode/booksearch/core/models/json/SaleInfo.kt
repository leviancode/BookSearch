package com.leviancode.booksearch.core.models.json

data class SaleInfo(
    var buyLink: String = "",
    var country: String = "",
    var isEbook: Boolean = false,
    var listPrice: ListPrice = ListPrice(),
    var offers: List<Offer> = listOf(),
    var retailPrice: RetailPriceX = RetailPriceX(),
    var saleability: String = ""
)