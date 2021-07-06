package com.leviancode.booksearch.core.models.json

data class Item(
    var accessInfo: AccessInfo = AccessInfo(),
    var etag: String = "",
    var id: String = "",
    var kind: String = "",
    var saleInfo: SaleInfo = SaleInfo(),
    var searchInfo: SearchInfo = SearchInfo(),
    var selfLink: String = "",
    var volumeInfo: VolumeInfo = VolumeInfo()
)