package com.devgc.mystickeralbum.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.extensions.toGrid
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.AlbumStatus
import com.devgc.mystickeralbum.model.ButtonItem
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.StickersList
import com.devgc.mystickeralbum.ui.components.AlbumCard
import com.devgc.mystickeralbum.ui.components.AlbumStickerInfo
import com.devgc.mystickeralbum.ui.components.SimpleDialog
import com.devgc.mystickeralbum.ui.components.TitleSection
import com.devgc.mystickeralbum.ui.stateholders.UpdateAlbumUIState
import com.devgc.mystickeralbum.ui.theme.BorderColor
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.devgc.mystickeralbum.ui.viewmodels.UpdateAlbumViewModel

@Composable
fun UpdateAlbumUIScreen(viewModel: UpdateAlbumViewModel) {
    val state = viewModel.uiState.collectAsState().value
    UpdateAlbumUIScreen(state)

    if (state.showDeleteAlbumDialog) {
        DeleteAlbumDialog(state)
    }
}

@Composable
fun DeleteAlbumDialog(state: UpdateAlbumUIState) {
    SimpleDialog(
        title = stringResource(id = R.string.delete_album_title),
        description = stringResource(id = R.string.confirm_delete_album_desc, state.album.name),
        negativeButton = ButtonItem(
            text = stringResource(id = R.string.cancel_button),
            onClick = state.onCloseDeleteAlbumDialog
        ),
        positiveButton = ButtonItem(
            text = stringResource(id = R.string.confirm_button),
            onClick = { state.onConfirmDeleteAlbumDialog() }
        )
    )
}

@Composable
fun UpdateAlbumUIScreen(state: UpdateAlbumUIState) {
    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            AlbumView(state.album)
        }

        item {
            CopyStickersButtons(state)
        }

        item {
            TitleSection(
                title = stringResource(id = R.string.sticker_grid_title),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        stickersGrid(state)
    }

    if (state.showStickerDialog) {
        StickerOptionsDialog(state)
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


fun LazyListScope.stickersGrid(state: UpdateAlbumUIState) {
    val columns = 6
    val grid = state.album.stickersList.stickers.toGrid(columns)

    items(grid) { row ->
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            row.forEach { sticker ->
                StickerItem(sticker = sticker, state = state)
            }

            repeat(columns - row.size) {
                Spacer(modifier = Modifier.weight(1F))
            }
        }
    }
}

@Composable
fun RowScope.StickerItem(sticker: Sticker, state: UpdateAlbumUIState) {
    Box(
        modifier = Modifier
            .weight(1F)
            .aspectRatio(1F)
            .shadow(4.dp, RoundedCornerShape(8.dp), clip = false)
            .background(
                if (sticker.found) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primaryContainer,
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
            fontWeight = if (sticker.found) FontWeight.Normal else FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        if (sticker.found) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .aspectRatio(1F)
                    .align(Alignment.TopEnd)
                    .background(
                        MaterialTheme.colorScheme.secondary,
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
                    color = MaterialTheme.colorScheme.onSecondary,
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
        onDismissRequest = { state.onCloseStickerDialog() }
    ) {
        Column(
            modifier = Modifier
                .width(200.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(10.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column {
                Text(
                    text = (stringResource(id = R.string.sticker_title) + " " + state.stickerDialog.identifier).uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = (if (state.stickerDialog.found) stringResource(id = R.string.found_title) else stringResource(
                            id = R.string.not_found_title
                        )).uppercase(),
                        fontSize = 8.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(y = (-4).dp)
                            .border(
                                1.dp,
                                BorderColor,
                                RoundedCornerShape(4.dp)
                            )
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        letterSpacing = (0.1).sp
                    )
                }
            }

            if (!state.stickerDialog.found) {
                Button(
                    onClick = { state.onFoundNotFoundClick(true) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
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
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            } else {
                Button(
                    onClick = { state.onFoundNotFoundClick(false) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
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
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }


                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.repeated_stickers_title).uppercase(),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        letterSpacing = (0.1).sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
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
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1F)
                                .shadow(4.dp, CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    CircleShape
                                )
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
                                    .align(Alignment.Center),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        Image(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1F)
                                .shadow(4.dp, CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    CircleShape
                                )
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

@Composable
fun CopyStickersButtons(state: UpdateAlbumUIState) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .weight(1F)
                .shadow(4.dp, RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp))
                .clickable {
                    state.onCopyMissingStickersClick(context)
                }
                .padding(vertical = 6.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_copy),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
            )

            Text(
                text = stringResource(id = R.string.copy_missing_stickers),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Row(
            modifier = Modifier
                .weight(1F)
                .shadow(4.dp, RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp))
                .clickable {
                    state.onCopyRepeatedStickersClick(context)
                }
                .padding(vertical = 6.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_copy),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
            )

            Text(
                text = stringResource(id = R.string.copy_repeated_stickers),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
            )
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
        UpdateAlbumUIScreen(
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