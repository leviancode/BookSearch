package com.leviancode.booksearch.core.models.json

data class VolumeInfo(
    var allowAnonLogging: Boolean = false,
    var authors: List<String> = listOf(),
    var averageRating: Double = 0.0,
    var canonicalVolumeLink: String = "",
    var categories: List<String> = listOf(),
    var contentVersion: String = "",
    var description: String = "",
    var imageLinks: ImageLinks = ImageLinks(),
    var industryIdentifiers: List<IndustryIdentifier> = listOf(),
    var infoLink: String = "",
    var language: String = "",
    var maturityRating: String = "",
    var pageCount: Int = 0,
    var panelizationSummary: PanelizationSummary = PanelizationSummary(),
    var previewLink: String = "",
    var printType: String = "",
    var publishedDate: String = "",
    var publisher: String = "",
    var ratingsCount: Int = 0,
    var readingModes: ReadingModes = ReadingModes(),
    var subtitle: String = "",
    var title: String = ""
)