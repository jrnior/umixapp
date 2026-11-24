package com.example.umix.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("search")
    suspend fun searchAlbums(
        @Query("term") term: String,
        @Query("entity") entity: String = "album",
        @Query("country") country: String = "BR",
        @Query("limit") limit: Int = 25
    ): ItunesResponse
}