package com.example.mystickeralbum.activities

import android.R.attr.value
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.example.mystickeralbum.stateholders.AlbumsUIState
import com.example.mystickeralbum.ui.AlbumCard
import com.example.mystickeralbum.ui.AlbumStickerInfo
import com.example.mystickeralbum.ui.TopBar
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.example.mystickeralbum.viewmodels.AlbumsViewModel

class AlbumsActivity : ComponentActivity() {

    private val viewModel: AlbumsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStickerAlbumTheme {
                AlbumsScreen(
                    state = viewModel.uiState.collectAsState().value
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateAlbumsList()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AlbumsScreen(state: AlbumsUIState) {
        Scaffold(
            floatingActionButton = {
                AddAlbumFab(state.onFabClick)
            },
            topBar = {
                AlbumsTopBar()
            }
        ) {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                AlbumsList(state)
            }
        }
    }

    @Composable
    fun AlbumsTopBar() {
        TopBar(title = R.string.album_list_title)
    }

    @Composable
    fun AlbumsList(state: AlbumsUIState) {
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
    fun AlbumItem(album: Album, state: AlbumsUIState) {
        AlbumCard(
            album = album,
            activity = this,
            onClick = state.onAlbumClick
        ) {
            AlbumStickerInfo(album)
        }
    }

    @Composable
    fun AddAlbumFab(onClick: (Activity) -> Unit) {
        SmallFloatingActionButton(
            onClick = { onClick(this) },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = null
            )
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun AlbumsScreenPreview() {
        MyStickerAlbumTheme {
            AlbumsScreen(
                state = AlbumsUIState(
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
}