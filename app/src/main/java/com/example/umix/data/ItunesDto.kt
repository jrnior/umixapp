package com.example.umix.data

import kotlinx.serialization.Serializable

@Serializable
data class ItunesResponse(
    val resultCount: Int = 0,
    val results: List<ItunesItem> = emptyList()
)

@Serializable
data class ItunesItem(
    val wrapperType: String? = null,
    val collectionId: Long? = null,
    val collectionName: String? = null,
    val artistName: String? = null,
    val artworkUrl100: String? = null,
    val releaseDate: String? = null,
    val trackCount: Int? = null,
    val primaryGenreName: String? = null
)