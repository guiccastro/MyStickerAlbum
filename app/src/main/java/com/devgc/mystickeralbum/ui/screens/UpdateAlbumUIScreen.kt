package com.devgc.mystickeralbum.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.AlbumStatus
import com.devgc.mystickeralbum.model.ButtonItem
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.StickersList
import com.devgc.mystickeralbum.ui.components.AlbumCard
import com.devgc.mystickeralbum.ui.components.AlbumStickerInfo
import com.devgc.mystickeralbum.ui.components.IconsLegendDialog
import com.devgc.mystickeralbum.ui.components.SimpleDialog
import com.devgc.mystickeralbum.ui.components.TextField
import com.devgc.mystickeralbum.ui.stateholders.UpdateAlbumUIState
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.devgc.mystickeralbum.ui.theme.Poppins
import com.devgc.mystickeralbum.ui.viewmodels.UpdateAlbumViewModel

@Composable
fun UpdateAlbumUIScreen(viewModel: UpdateAlbumViewModel) {
    val state = viewModel.uiState.collectAsState().value
    UpdateAlbumUIScreen(state)

    if (state.showDeleteAlbumDialog) {
        DeleteAlbumDialog(state)
    }

    if (state.showIconsLegendDialog) {
        IconsLegendDialog(state.changeIconsLegendDialogState)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UpdateAlbumUIScreen(state: UpdateAlbumUIState) {
    val lazyListState = rememberLazyListState()
    val configuration = LocalConfiguration.current
    val defaultColumns =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 10 else 5
    val columns = (state.columns ?: defaultColumns).coerceAtLeast(1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 6.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Header(state, defaultColumns)

        StickerGridHeader(
            title = stringResource(id = R.string.sticker_grid_title),
            isHeaderVisible = state.isHeaderVisible,
            onClick = state.onToggleHeader
        )

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            contentPadding = PaddingValues(top = 3.dp, bottom = 8.dp)
        ) {
            items(state.stickers.toStickerRows(columns)) { row ->
                when (row) {
                    StickerGridRow.Empty -> {
                        EmptyStickerRow(columns)
                    }

                    is StickerGridRow.Stickers -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            row.stickers.forEach { sticker ->
                                Box(
                                    modifier = Modifier
                                        .weight(1F)
                                ) {
                                    StickerItem(sticker, state)
                                }
                            }

                            repeat(columns - row.stickers.size) {
                                Spacer(
                                    modifier = Modifier
                                        .weight(1F)
                                )
                            }
                        }
                    }
                }
            }
        }

        ReturnToTopButton(state, lazyListState)
    }
}

@Composable
fun StickerGridHeader(
    title: String,
    isHeaderVisible: Boolean,
    onClick: () -> Unit
) {
    val color = MaterialTheme.colorScheme.onBackground

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(top = 2.dp, bottom = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1F)
                .background(color)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title.uppercase(),
                color = color,
                fontSize = 19.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                style = Poppins
            )

            Icon(
                imageVector = if (isHeaderVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }

        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1F)
                .background(color)
        )
    }
}

@Composable
private fun EmptyStickerRow(columns: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        repeat(columns) {
            Spacer(
                modifier = Modifier
                    .weight(1F)
                    .aspectRatio(2F)
            )
        }
    }
}

private sealed interface StickerGridRow {
    data class Stickers(val stickers: List<Sticker>) : StickerGridRow
    data object Empty : StickerGridRow
}

private fun List<Sticker>.toStickerRows(columns: Int): List<StickerGridRow> {
    val safeColumns = columns.coerceAtLeast(1)
    val rows = ArrayList<StickerGridRow>()
    val currentRow = ArrayList<Sticker>()

    forEach { sticker ->
        currentRow.add(sticker)

        if (currentRow.size == safeColumns || sticker.lineBreakAfter) {
            rows.add(StickerGridRow.Stickers(currentRow.toList()))
            currentRow.clear()

            if (sticker.extraLineAfter) {
                rows.add(StickerGridRow.Empty)
            }
        }
    }

    if (currentRow.isNotEmpty()) {
        rows.add(StickerGridRow.Stickers(currentRow.toList()))
    }

    return rows
}

@Composable
fun Header(state: UpdateAlbumUIState, defaultColumns: Int) {
    AnimatedVisibility(
        visible = state.isHeaderVisible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        HeaderContent(state, defaultColumns)
    }
}

@Composable
fun HeaderContent(state: UpdateAlbumUIState, defaultColumns: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AlbumView(state.album)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    state.onViewAll()
                },
                contentPadding = PaddingValues(4.dp),
                modifier = Modifier
                    .weight(1F)
                    .height(42.dp)
            ) {
                Text("Todas")
            }

            Button(
                onClick = {
                    state.onViewMissing()
                },
                contentPadding = PaddingValues(4.dp),
                modifier = Modifier
                    .weight(1F)
                    .height(42.dp)
            ) {
                Text("Faltantes")
            }

            Button(
                onClick = {
                    state.onViewRepeated()
                },
                contentPadding = PaddingValues(4.dp),
                modifier = Modifier
                    .weight(1F)
                    .height(42.dp)
            ) {
                Text("Repetidas", overflow = TextOverflow.Ellipsis, maxLines = 1)
            }
        }

        SearchSticker(state, defaultColumns)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchSticker(state: UpdateAlbumUIState, defaultColumns: Int) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier
            .height(46.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight()
        ) {
            TextField(
                text = state.searchStickerTextField.text,
                onValueChange = state.searchStickerTextField.onTextChange,
                modifier = Modifier
                    .fillMaxHeight(),
                placeholderText = stringResource(id = R.string.search_sticker_placeholder),
                textSize = 14.sp,
                textStyle = Poppins,
                trailingIcon = {
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            state.onClearTextField()
                        },
                        modifier = Modifier
                            .width(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Filled.Clear, contentDescription = null)
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            TextField(
                text = (state.columns ?: defaultColumns).toString(),
                onValueChange = {
                    state.onColumnsChanged(it.toIntOrNull())
                },
                placeholderText = "Colunas",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(58.dp)
            )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StickerItem(sticker: Sticker, state: UpdateAlbumUIState) {
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .aspectRatio(1F)
            .shadow(4.dp, RoundedCornerShape(8.dp), clip = false)
            .background(
                if (sticker.found) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primaryContainer,
                RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = { state.onStickerClick(sticker) },
                onLongClick = { showMenu = true }
            )
    ) {
        Text(
            text = sticker.identifier,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.Center),
            textDecoration = if (sticker.found) TextDecoration.LineThrough else null,
            fontWeight = if (sticker.found) FontWeight.Normal else FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center
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

            Box(
                modifier = Modifier
                    .size(20.dp)
                    .aspectRatio(1F)
                    .align(Alignment.BottomStart)
                    .background(
                        Color.Red,
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 8.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 4.dp
                        )
                    )
                    .clickable {
                        state.onRemoveSticker(sticker)
                    }
            ) {
                Icon(
                    painterResource(R.drawable.ic_delete),
                    contentDescription = "Remove",
                    modifier = Modifier
                        .padding(2.dp)
                )
            }
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(
                            id = if (sticker.lineBreakAfter) {
                                R.string.remove_sticker_line_break
                            } else {
                                R.string.add_sticker_line_break
                            }
                        )
                    )
                },
                onClick = {
                    showMenu = false
                    state.onToggleStickerLineBreak(sticker)
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(
                            id = if (sticker.extraLineAfter) {
                                R.string.remove_sticker_extra_line
                            } else {
                                R.string.add_sticker_extra_line
                            }
                        )
                    )
                },
                onClick = {
                    showMenu = false
                    state.onToggleStickerExtraLine(sticker)
                }
            )
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

@Composable
fun ReturnToTopButton(state: UpdateAlbumUIState, lazyListState: LazyListState) {
    val scope = rememberCoroutineScope()
    AnimatedVisibility(
        visible = state.showReturnToTopButton,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_return_top),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(38.dp)
                    .background(MaterialTheme.colorScheme.tertiary, CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        state.onReturnToTopButtonClick(lazyListState, scope)
                    }
                    .padding(6.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun StickersListPreview() {

    val stickers = listOf(
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

    MyStickerAlbumTheme {
        UpdateAlbumUIScreen(
            state = UpdateAlbumUIState(
                album = Album(
                    name = "Album Name",
                    stickersList = StickersList(
                        stickers = stickers
                    ),
                    status = AlbumStatus.Completing,
                    albumImage = ""
                ),
                showReturnToTopButton = true,
                stickers = stickers
            )
        )
    }
}
