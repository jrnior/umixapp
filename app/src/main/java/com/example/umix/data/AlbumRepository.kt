package com.example.umix.data

import kotlin.coroutines.cancellation.CancellationException

class AlbumRepository(
    private val api: ItunesApi = NetworkModule.itunesApi
) {
    suspend fun search(term: String): Result<List<Album>> = try {
        val albums = api.searchAlbums(term).results.mapNotNull { it.toAlbum() }
        Result.success(albums)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

private fun ItunesItem.toAlbum(): Album? {
    val id = collectionId ?: return null
    return Album(
        id = id,
        title = collectionName.orEmpty(),
        artist = artistName.orEmpty(),
        coverUrl = artworkUrl100?.replace("100x100bb", "300x300bb"),
        year = releaseDate?.take(4),
        genre = primaryGenreName,
        trackCount = trackCount
    )
}