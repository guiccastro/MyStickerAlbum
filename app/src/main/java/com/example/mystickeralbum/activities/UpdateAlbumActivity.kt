package com.example.mystickeralbum.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystickeralbum.R
import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.model.StickersList
import com.example.mystickeralbum.stateholders.UpdateAlbumUIState
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.example.mystickeralbum.viewmodels.AlbumsViewModel
import com.example.mystickeralbum.viewmodels.UpdateAlbumViewModel


class UpdateAlbumActivity : ComponentActivity() {

    private val viewModel: UpdateAlbumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val albumName = intent.getStringExtra(AlbumsViewModel.ALBUM_NAME) ?: ""
        Log.println(Log.ASSERT, "AlbumName", albumName)

        setContent {
            MyStickerAlbumTheme {
                UpdateAlbumScreen(viewModel.uiState.collectAsState().value)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UpdateAlbumScreen(state: UpdateAlbumUIState) {
        Scaffold(
            topBar = {
                UpdateAlbumTopBar()
            }
        ) {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                StickersGrid(state.album.stickersList)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UpdateAlbumTopBar() {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.update_album_title),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    }

    @Composable
    fun StickersGrid(stickersList: StickersList) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        ) {
            items(stickersList.stickers) {
                StickerItem(sticker = it)
            }
        }
    }

    @Composable
    fun StickerItem(sticker: Sticker) {
        Box(
            modifier = Modifier
                .aspectRatio(1F)
                .shadow(4.dp, RoundedCornerShape(8.dp), clip = false)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .background(
                    if (sticker.found) Color.Gray else Color.LightGray,
                    RoundedCornerShape(8.dp)
                )
        ) {
            Text(
                text = sticker.identifier,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.Center)
            )

            if (sticker.found) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .aspectRatio(1F)
                        .align(Alignment.TopEnd)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 8.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 4.dp
                            )
                        )
                ) {
                    Text(
                        text = sticker.repeated.toString(),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun StickersListPreview() {
        MyStickerAlbumTheme {
            UpdateAlbumScreen(
                state = UpdateAlbumUIState(
                    album = Album(
                        name = "Album Name",
                        stickersList = StickersList(
                            stickers = listOf(
                                Sticker(
                                    "1",
                                    false,
                                    0
                                ),
                                Sticker(
                                    "2",
                                    true,
                                    0
                                ),
                                Sticker(
                                    "A1",
                                    true,
                                    14
                                ),
                                Sticker(
                                    "1",
                                    false,
                                    0
                                ),
                                Sticker(
                                    "2",
                                    true,
                                    0
                                ),
                                Sticker(
                                    "A1",
                                    true,
                                    14
                                ),
                                Sticker(
                                    "1",
                                    false,
                                    0
                                ),
                                Sticker(
                                    "2",
                                    true,
                                    0
                                ),
                                Sticker(
                                    "A1",
                                    true,
                                    14
                                ),
                                Sticker(
                                    "1",
                                    false,
                                    0
                                ),
                                Sticker(
                                    "2",
                                    true,
                                    0
                                ),
                                Sticker(
                                    "A1",
                                    true,
                                    14
                                )
                            )
                        ),
                        status = AlbumStatus.Completing,
                        albumImage = ""
                    )
                )
            )
        }
    }
}