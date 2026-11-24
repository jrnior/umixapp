package com.example.umix.data

data class Album(
    val id: Long,
    val title: String,
    val artist: String,
    val coverUrl: String?,
    val year: String?,
    val genre: String?,
    val trackCount: Int?
)