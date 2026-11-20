package me.echeung.moemoekyun.domain.songs.model

import me.echeung.moemoekyun.client.model.Song
import me.echeung.moemoekyun.client.model.SongDescriptor
import me.echeung.moemoekyun.util.PreferenceUtil
import me.echeung.moemoekyun.util.ext.formatDuration
import javax.inject.Inject

class SongConverter @Inject constructor(private val preferenceUtil: PreferenceUtil) {

    fun toDomainSong(song: Song): DomainSong = DomainSong(
        id = song.id,
        title = if (preferenceUtil.shouldPreferRomaji().get()) {
            (song.titleRomaji ?: song.title).orEmpty().trim()
        } else {
            song.title.orEmpty().trim()
        },
        artists = song.artists?.toDomainSong(),
        albums = song.albums?.toDomainSong(),
        sources = song.sources?.toDomainSong(),
        duration = song.duration(),
        durationSeconds = song.duration.toLong(),
        albumArtUrl = null,
        favorited = song.favorite,
        favoritedAtEpoch = song.favoritedAt,
    )

    private fun List<SongDescriptor>.toDomainSong(): String = this
        .mapNotNull {
            val preferredName = if (preferenceUtil.shouldPreferRomaji().get()) {
                it.nameRomaji
            } else {
                it.name
            }
            preferredName ?: it.name
        }
        .joinToString()

    private fun Song.duration(): String = duration.toLong().formatDuration()
}
