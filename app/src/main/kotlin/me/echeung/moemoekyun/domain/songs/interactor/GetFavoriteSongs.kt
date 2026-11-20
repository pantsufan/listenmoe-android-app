package me.echeung.moemoekyun.domain.songs.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import me.echeung.moemoekyun.domain.songs.LocalFavoritesRepository
import me.echeung.moemoekyun.domain.songs.model.DomainSong
import me.echeung.moemoekyun.domain.user.UserService
import me.echeung.moemoekyun.util.PreferenceUtil
import me.echeung.moemoekyun.util.SongsSorter
import me.echeung.moemoekyun.util.SortType
import javax.inject.Inject

class GetFavoriteSongs @Inject constructor(
    private val userService: UserService,
    private val localFavoritesRepository: LocalFavoritesRepository,
    private val songsSorter: SongsSorter,
    private val preferenceUtil: PreferenceUtil,
) {

    fun asFlow(): Flow<List<DomainSong>> = combine(
        userService.state,
        localFavoritesRepository.flow,
        preferenceUtil.favoritesSortType().asFlow(),
        preferenceUtil.favoritesSortDescending().asFlow(),
    ) { _, _, _, _ -> }
        .map { getAll() }

    fun getAll(): List<DomainSong> {
        val serverFavorites = userService.state.value.favorites
        val localFavorites = localFavoritesRepository.getAll()
        val serverIds = serverFavorites.map { it.id }.toSet()
        val combined = serverFavorites + localFavorites.filterNot { it.id in serverIds }

        return songsSorter.sort(
            combined,
            preferenceUtil.favoritesSortType().get(),
            preferenceUtil.favoritesSortDescending().get(),
        )
    }

    fun isFavorite(songId: Int): Boolean = localFavoritesRepository.isFavorite(songId) ||
        userService.state.value.favorites.any { it.id == songId }

    fun setSortType(sortType: SortType) {
        preferenceUtil.favoritesSortType().set(sortType)
    }

    fun setSortDescending(descending: Boolean) {
        preferenceUtil.favoritesSortDescending().set(descending)
    }
}
