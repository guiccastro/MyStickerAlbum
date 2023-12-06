package com.example.mystickeralbum.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mystickeralbum.R
import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.model.StickersList
import com.example.mystickeralbum.model.TopBarItem
import com.example.mystickeralbum.stateholders.UpdateAlbumUIState
import com.example.mystickeralbum.ui.AlbumCard
import com.example.mystickeralbum.ui.AlbumStickerInfo
import com.example.mystickeralbum.ui.TopBar
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.example.mystickeralbum.viewmodels.AlbumsViewModel
import com.example.mystickeralbum.viewmodels.UpdateAlbumViewModel


class UpdateAlbumActivity : ComponentActivity() {

    private val viewModel: UpdateAlbumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val albumName = intent.getStringExtra(AlbumsViewModel.ALBUM_NAME) ?: ""

        setContent {
            MyStickerAlbumTheme {
                val state = viewModel.uiState.collectAsState().value
                state.onReceivedAlbumName(albumName)
                UpdateAlbumScreen(state)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UpdateAlbumScreen(state: UpdateAlbumUIState) {
        Scaffold(
            topBar = {
                UpdateAlbumTopBar(state)
            }
        ) {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    AlbumView(state.album)
                    StickersGrid(state)
                }

                if (state.showDialog) {
                    StickerOptionsDialog(state)
                }
            }
        }
    }

    @Composable
    fun AlbumView(album: Album) {
        AlbumCard(
            album = album
        ) {
            AlbumStickerInfo(album)
        }
    }

    @Composable
    fun UpdateAlbumTopBar(state: UpdateAlbumUIState) {
        TopBar(
            title = R.string.update_album_title,
            onReturn = { finish() },
            itemsList = listOf(
                TopBarItem(R.drawable.ic_delete) { state.onDeleteAlbumClick() },
                TopBarItem(R.drawable.ic_edit) { state.onEditAlbumClick(this) }
            )
        )
    }

    @Composable
    fun StickersGrid(state: UpdateAlbumUIState) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        ) {
            items(state.album.stickersList.stickers) {
                StickerItem(sticker = it, state = state)
            }
        }
    }

    @Composable
    fun StickerItem(sticker: Sticker, state: UpdateAlbumUIState) {
        Box(
            modifier = Modifier
                .aspectRatio(1F)
                .shadow(4.dp, RoundedCornerShape(8.dp), clip = false)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .background(
                    if (sticker.found) Color.DarkGray else Color.LightGray,
                    RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable { state.onStickerClick(sticker) }
        ) {
            Text(
                text = sticker.identifier,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.Center),
                textDecoration = if (sticker.found) TextDecoration.LineThrough else null,
                fontWeight = if (sticker.found) FontWeight.Normal else FontWeight.SemiBold
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
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    @Composable
    fun StickerOptionsDialog(state: UpdateAlbumUIState) {
        Dialog(
            onDismissRequest = { state.onCloseDialog() }
        ) {
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .background(Color.LightGray, RoundedCornerShape(10.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.sticker_title) + " " + state.stickerDialog.identifier,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = if (state.stickerDialog.found) stringResource(id = R.string.found_title) else stringResource(
                                id = R.string.not_found_title
                            ),
                            fontSize = 8.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(y = (-4).dp)
                                .border(
                                    1.dp,
                                    Color.Black,
                                    RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 0.dp,
                                        bottomEnd = 4.dp,
                                        bottomStart = 4.dp
                                    )
                                )
                                .background(
                                    Color.Gray, RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 0.dp,
                                        bottomEnd = 4.dp,
                                        bottomStart = 4.dp
                                    )
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }

                if (!state.stickerDialog.found) {
                    Button(
                        onClick = { state.onFoundNotFoundClick(true) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(4.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.found_title),
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Button(
                        onClick = { state.onFoundNotFoundClick(false) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(4.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.not_found_title),
                            fontSize = 14.sp
                        )
                    }


                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.repeated_stickers_title),
                            fontSize = 10.sp,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Row(
                            modifier = Modifier
                                .height(30.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.ic_remove),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color.White),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1F)
                                    .shadow(4.dp, CircleShape)
                                    .background(Color.Gray, CircleShape)
                                    .clip(CircleShape)
                                    .clickable { state.onChangeRepeatedStickerClick(-1) }
                                    .padding(4.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(
                                    text = state.stickerDialog.repeated.toString(),
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                )
                            }

                            Image(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color.White),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1F)
                                    .shadow(4.dp, CircleShape)
                                    .background(Color.Gray, CircleShape)
                                    .clip(CircleShape)
                                    .clickable { state.onChangeRepeatedStickerClick(1) }
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun StickerOptionsDialogPreview() {
        MyStickerAlbumTheme {
            StickerOptionsDialog(
                state = UpdateAlbumUIState(
                    stickerDialog = Sticker(
                        "1",
                        true,
                        0
                    )
                )
            )
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