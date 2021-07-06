package com.leviancode.booksearch.core.models.json

data class AccessInfo(
    var accessViewStatus: String = "",
    var country: String = "",
    var embeddable: Boolean = false,
    var epub: Epub = Epub(),
    var pdf: Pdf = Pdf(),
    var publicDomain: Boolean = false,
    var quoteSharingAllowed: Boolean = false,
    var textToSpeechPermission: String = "",
    var viewability: String = "",
    var webReaderLink: String = ""
)