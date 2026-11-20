package me.echeung.moemoekyun.domain.songs

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import me.echeung.moemoekyun.domain.songs.model.DomainSong
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalFavoritesRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val file by lazy { File(context.filesDir, "local_favorites.json") }

    private val _flow = MutableStateFlow<List<DomainSong>>(emptyList())
    val flow: StateFlow<List<DomainSong>> = _flow.asStateFlow()

    init {
        try {
            if (file.exists()) {
                val content = file.readText()
                if (content.isNotBlank()) {
                    _flow.value = json.decodeFromString(ListSerializer(DomainSong.serializer()), content)
                }
            }
        } catch (_: Exception) {
            // Ignored on corrupt file
        }
    }

    private suspend fun persist(songs: List<DomainSong>) = withContext(Dispatchers.IO) {
        try {
            val content = json.encodeToString(ListSerializer(DomainSong.serializer()), songs)
            file.writeText(content)
        } catch (_: Exception) {
            // Ignored on write error
        }
    }

    fun getAll(): List<DomainSong> = _flow.value

    fun get(songId: Int): DomainSong? = _flow.value.find { it.id == songId }

    fun isFavorite(songId: Int): Boolean = _flow.value.any { it.id == songId }

    fun addFavorite(song: DomainSong) {
        val favoritedSong = song.copy(
            favorited = true,
            favoritedAtEpoch = song.favoritedAtEpoch ?: System.currentTimeMillis(),
        )
        _flow.update { current ->
            val updated = current.filterNot { it.id == song.id } + favoritedSong
            scope.launch { persist(updated) }
            updated
        }
    }

    fun removeFavorite(songId: Int) {
        _flow.update { current ->
            val updated = current.filterNot { it.id == songId }
            scope.launch { persist(updated) }
            updated
        }
    }
}
