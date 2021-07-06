package com.leviancode.booksearch.core.models.json

data class Offer(
    var finskyOfferType: Int = 0,
    var listPrice: ListPriceX = ListPriceX(),
    var retailPrice: RetailPrice = RetailPrice()
)