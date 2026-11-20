package me.echeung.moemoekyun.domain.songs.interactor

import me.echeung.moemoekyun.domain.songs.SongsService
import me.echeung.moemoekyun.domain.songs.model.DomainSong
import javax.inject.Inject

class FavoriteSong @Inject constructor(private val songsService: SongsService) {

    suspend fun await(songId: Int, fallbackSong: DomainSong? = null): Boolean =
        songsService.favorite(songId, fallbackSong).favorited
}
