package me.echeung.moemoekyun.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import me.echeung.moemoekyun.R
import me.echeung.moemoekyun.domain.songs.model.DomainSong
import me.echeung.moemoekyun.ui.common.songsItems
import me.echeung.moemoekyun.ui.theme.AppTheme
import me.echeung.moemoekyun.ui.util.plus
import me.echeung.moemoekyun.util.SortType

@Composable
fun UnauthedHomeContent(
    favorites: ImmutableList<DomainSong>?,
    query: String?,
    onQueryChange: (String) -> Unit,
    sortType: SortType,
    onSortBy: (SortType) -> Unit,
    sortDescending: Boolean,
    onSortDescending: (Boolean) -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateRegister: () -> Unit,
    onShowSongs: (ImmutableList<DomainSong>) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val hasFavorites = !favorites.isNullOrEmpty() || !query.isNullOrBlank()

    if (!hasFavorites) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding + 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.75f),
                painter = painterResource(R.drawable.logo),
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.local_favorites_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateRegister() },
                ) {
                    Text(stringResource(R.string.register))
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateLogin() },
                ) {
                    Text(stringResource(R.string.login))
                }
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = contentPadding,
        ) {
            item {
                LocalFavoritesHeader(
                    count = favorites?.size ?: 0,
                    onNavigateLogin = onNavigateLogin,
                )
            }

            item {
                FavoritesToolbar(
                    query = query,
                    onQueryChange = onQueryChange,
                    sortType = sortType,
                    onSortBy = onSortBy,
                    sortDescending = sortDescending,
                    onSortDescending = onSortDescending,
                    requestRandomSong = null,
                )
            }

            songsItems(songs = favorites, onShowSongs = onShowSongs)
        }
    }
}

@Composable
private fun LocalFavoritesHeader(count: Int, onNavigateLogin: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Column {
                Text(
                    text = stringResource(R.string.local_favorites),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = if (count == 1) {
                        stringResource(R.string.local_favorites_count_single)
                    } else {
                        stringResource(R.string.local_favorites_count, count)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        OutlinedButton(onClick = onNavigateLogin) {
            Text(stringResource(R.string.login))
        }
    }
}

@PreviewLightDark
@Composable
private fun UnauthedHomeContentPreview() {
    AppTheme {
        UnauthedHomeContent(
            favorites = null,
            query = null,
            onQueryChange = {},
            sortType = SortType.TITLE,
            onSortBy = {},
            sortDescending = false,
            onSortDescending = {},
            onNavigateLogin = {},
            onNavigateRegister = {},
            onShowSongs = {},
        )
    }
}
