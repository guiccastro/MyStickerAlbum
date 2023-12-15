package com.example.mystickeralbum.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystickeralbum.R
import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.model.StickersList
import com.example.mystickeralbum.ui.stateholders.AlbumsListUIState
import com.example.mystickeralbum.ui.components.AlbumCard
import com.example.mystickeralbum.ui.components.AlbumStickerInfo
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.example.mystickeralbum.ui.viewmodels.AlbumsListViewModel

@Composable
fun AlbumsListUIScreen(viewModel: AlbumsListViewModel) {
    val state = viewModel.uiState.collectAsState().value
    AlbumsListUIScreen(state)
}

@Composable
fun AlbumsListUIScreen(state: AlbumsListUIState) {
    LaunchedEffect(Unit) {
        state.updateAlbumsList()
    }
    if (state.albumsList.isEmpty()) {
        Text(
            text = stringResource(id = R.string.album_list_empty),
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn {
            items(state.albumsList) {
                AlbumItem(
                    album = it,
                    state = state
                )
            }
        }
    }
}

@Composable
fun AlbumItem(album: Album, state: AlbumsListUIState) {
    AlbumCard(
        album = album,
        onClick = state.onAlbumClick
    ) {
        AlbumStickerInfo(album)
    }
}

@Preview(showSystemUi = true)
@Composable
fun AlbumsScreenPreview() {
    MyStickerAlbumTheme {
        AlbumsListUIScreen(
            state = AlbumsListUIState(
                albumsList = listOf(
                    Album(
                        "Album 1",
                        StickersList(
                            listOf(
                                Sticker("1", false, 0),
                                Sticker("2", true, 1),
                                Sticker("3", true, 2)
                            )
                        ),
                        AlbumStatus.Completing,
                        "https://i0.wp.com/maquinadoesporte.com.br/wp-content/uploads/2023/10/foto-maquina-do-esporte-1200-675-3-8.png?fit=616%2C308&ssl=1"
                    ),
                    Album(
                        "Album 2",
                        StickersList(
                            listOf(
                                Sticker("1", false, 0),
                                Sticker("2", false, 0),
                                Sticker("3", false, 0)
                            )
                        ),
                        AlbumStatus.Completing,
                        "https://ultraverso.com.br/wp-content/uploads/2023/07/2-1.jpg"
                    )
                )
            )
        )
    }
}