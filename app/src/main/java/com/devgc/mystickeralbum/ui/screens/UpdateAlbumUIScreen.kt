package com.devgc.mystickeralbum.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.booleanResource
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
import com.devgc.mystickeralbum.ui.components.IconsLegendDialog
import com.devgc.mystickeralbum.ui.components.SimpleDialog
import com.devgc.mystickeralbum.ui.components.TextField
import com.devgc.mystickeralbum.ui.components.TitleSection
import com.devgc.mystickeralbum.ui.stateholders.UpdateAlbumUIState
import com.devgc.mystickeralbum.ui.theme.BorderColor
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

@Composable
fun UpdateAlbumUIScreen(state: UpdateAlbumUIState) {
    val isTablet = booleanResource(id = R.bool.isTablet)
    val lazyListState = rememberLazyListState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                state.onScroll(lazyListState.firstVisibleItemIndex)
                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        state = lazyListState,
        modifier = Modifier
            .nestedScroll(nestedScrollConnection)
    ) {
        item {
            AlbumView(state.album)
        }

//        item {
//            CopyStickersButtons(state)
//        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        state.onViewAll()
                    },
                    contentPadding = PaddingValues(4.dp),
                    modifier = Modifier
                        .weight(1F)
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
                ) {
                    Text("Repetidas", overflow = TextOverflow.Ellipsis, maxLines = 1)
                }
            }
        }

        item {
            SearchSticker(state, lazyListState)
        }

        item {
            TitleSection(
                title = stringResource(id = R.string.sticker_grid_title),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        stickersGrid(state, isTablet)
    }

    ReturnToTopButton(state, lazyListState)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchSticker(state: UpdateAlbumUIState, lazyListState: LazyListState) {
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                text = state.columns?.toString() ?: "",
                onValueChange = {
                    state.onColumnsChanged(it.toIntOrNull())
                },
                placeholderText = "Colunas",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(60.dp)
            )
        }

//        Button(
//            onClick = {
//                keyboardController?.hide()
//                state.onSearchStickerClick(lazyListState, scope)
//            },
//            modifier = Modifier
//                .fillMaxHeight(),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.secondary
//            )
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_search),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxHeight(),
//                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
//            )
//        }
//
//        Button(
//            onClick = {
//                keyboardController?.hide()
//                state.onFilterStickerClick()
//            },
//            modifier = Modifier
//            .fillMaxHeight(),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.secondary
//            )
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_filter),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxHeight(),
//                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
//            )
//        }
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

fun LazyListScope.stickersGrid(state: UpdateAlbumUIState, isTablet: Boolean) {
    val columns = state.columns ?:
        if (isTablet) UpdateAlbumViewModel.tabletColumnsGrid else UpdateAlbumViewModel.normalColumnsGrid
    val stickers = state.stickers
    val grid = stickers.toGrid(columns)

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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = state.showReturnToTopButton,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_return_top),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(40.dp)
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
                showSearchStickerTextField = true,
                showReturnToTopButton = true,
                stickers = stickers
            )
        )
    }
}