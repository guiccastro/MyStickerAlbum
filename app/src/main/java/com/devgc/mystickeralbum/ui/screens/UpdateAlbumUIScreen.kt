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
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.AlbumStatus
import com.devgc.mystickeralbum.model.ButtonItem
import com.devgc.mystickeralbum.model.ImageCropperHelper
import com.devgc.mystickeralbum.model.ImageCropperHelper.getImageState
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.StickersList
import com.devgc.mystickeralbum.ui.components.IconsLegendDialog
import com.devgc.mystickeralbum.ui.components.SimpleDialog
import com.devgc.mystickeralbum.ui.components.TextField
import com.devgc.mystickeralbum.ui.stateholders.StickerFilter
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
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF071827),
                        Color(0xFF0B2D4A),
                        Color(0xFF071420)
                    )
                )
            )
            .padding(start = 12.dp, top = 10.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 2.dp, bottom = 12.dp)
        ) {
            itemsIndexed(state.stickers.toStickerRows(columns)) { rowIndex, row ->
                when (row) {
                    StickerGridRow.Empty -> {
                        EmptyStickerRow(columns)
                    }

                    is StickerGridRow.Stickers -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            row.stickers.forEach { sticker ->
                                Box(
                                    modifier = Modifier
                                        .weight(1F)
                                ) {
                                    StickerItem(sticker, state, rowIndex)
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
    val color = Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.07F))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1F)
                .background(color.copy(alpha = 0.25F))
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title.uppercase(),
                color = color,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                style = Poppins
            )

            Icon(
                imageVector = if (isHeaderVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(22.dp)
            )
        }

        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1F)
                .background(color.copy(alpha = 0.25F))
        )
    }
}

@Composable
private fun EmptyStickerRow(columns: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(columns) {
            Spacer(
                modifier = Modifier
                    .weight(1F)
                    .aspectRatio(2.2F)
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
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CollectionSummary(state.album)
        StickerStatsPanel(state.album)
        FilterControls(state)
        SearchSticker(state, defaultColumns)
        CopyStickersButtons(state)
    }
}

@Composable
fun CollectionSummary(album: Album) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        AlbumCover(album)

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = album.name,
                color = Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Poppins
            )

            Text(
                text = stringResource(id = R.string.album_primary_subtitle),
                color = Color.White.copy(alpha = 0.72F),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Poppins
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(70.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { album.getProgress() },
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF37D4B5),
                    trackColor = Color.White.copy(alpha = 0.18F),
                    strokeWidth = 7.dp
                )

                Text(
                    text = album.getFormattedProgress(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    style = Poppins
                )
            }

            Text(
                text = stringResource(id = R.string.album_progress_complete),
                color = Color.White.copy(alpha = 0.72F),
                fontSize = 12.sp,
                style = Poppins
            )
        }
    }
}

@Composable
fun AlbumCover(album: Album) {
    val context = LocalContext.current
    val imageState = ImageCropperHelper.imageStateHolder.collectAsState().value
    val bitmap = imageState.getImageState(context, album.albumImage)

    Box(
        modifier = Modifier
            .size(76.dp)
            .shadow(8.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF35A9E8),
                        Color(0xFF16598F)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = album.name.take(2).uppercase(),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                style = Poppins
            )
        }
    }
}

@Composable
fun StickerStatsPanel(album: Album) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color.White.copy(alpha = 0.08F))
            .border(1.dp, Color.White.copy(alpha = 0.14F), shape)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StickerStatItem(
            icon = R.drawable.ic_stat_total,
            label = stringResource(id = R.string.album_item_total),
            value = album.getTotalStickers().toString(),
            color = Color.White,
            modifier = Modifier.weight(1F)
        )
        StatDivider()
        StickerStatItem(
            icon = R.drawable.ic_stat_owned,
            label = stringResource(id = R.string.album_item_owned),
            value = album.getFound().size.toString(),
            color = Color(0xFF6EE7B7),
            modifier = Modifier.weight(1F)
        )
        StatDivider()
        StickerStatItem(
            icon = R.drawable.ic_stat_missing,
            label = stringResource(id = R.string.album_item_missing),
            value = album.getMissing().size.toString(),
            color = Color(0xFFFF7A88),
            modifier = Modifier.weight(1F)
        )
        StatDivider()
        StickerStatItem(
            icon = R.drawable.ic_stat_repeated,
            label = stringResource(id = R.string.album_item_repeated),
            value = album.getRepeated().size.toString(),
            color = Color(0xFFFFD166),
            modifier = Modifier.weight(1F)
        )
    }
}

@Composable
fun StickerStatItem(
    icon: Int,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            colorFilter = ColorFilter.tint(color)
        )

        Text(
            text = label,
            color = Color.White.copy(alpha = 0.68F),
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = Poppins
        )

        Text(
            text = value,
            color = color,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = Poppins
        )
    }
}

@Composable
fun StatDivider() {
    Box(
        modifier = Modifier
            .height(54.dp)
            .width(1.dp)
            .background(Color.White.copy(alpha = 0.16F))
    )
}

@Composable
fun FilterControls(state: UpdateAlbumUIState) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(14.dp)
    val filters = listOf(
        StickerFilter.All,
        StickerFilter.Missing,
        StickerFilter.Repeated
    )

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(shape)
                .background(Color.White.copy(alpha = 0.10F))
                .border(1.dp, Color.White.copy(alpha = 0.18F), shape)
                .clickable { expanded = true }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = null,
                tint = Color(0xFF6EE7B7),
                modifier = Modifier.size(20.dp)
            )

            Column(
                modifier = Modifier.weight(1F),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.sticker_filter_label),
                    color = Color.White.copy(alpha = 0.56F),
                    fontSize = 10.sp,
                    maxLines = 1,
                    style = Poppins
                )

                Text(
                    text = stringResource(id = state.selectedFilter.labelRes()),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Poppins
                )
            }

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.92F)
                .background(Color(0xFF123A5D))
        ) {
            filters.forEach { filter ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (state.selectedFilter == filter) {
                                            Color(0xFF6EE7B7)
                                        } else {
                                            Color.White.copy(alpha = 0.24F)
                                        }
                                    )
                            )

                            Text(
                                text = stringResource(id = filter.labelRes()),
                                color = Color.White,
                                fontWeight = if (state.selectedFilter == filter) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                },
                                style = Poppins
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        state.onFilterSelected(filter)
                    }
                )
            }
        }
    }
}

private fun StickerFilter.labelRes(): Int {
    return when (this) {
        StickerFilter.All -> R.string.sticker_filter_all
        StickerFilter.Missing -> R.string.sticker_filter_missing
        StickerFilter.Repeated -> R.string.sticker_filter_repeated
    }
}

private fun UpdateAlbumUIState.onFilterSelected(filter: StickerFilter) {
    when (filter) {
        StickerFilter.All -> onViewAll()
        StickerFilter.Missing -> onViewMissing()
        StickerFilter.Repeated -> onViewRepeated()
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
                textColor = Color.White,
                borderColor = Color.White.copy(alpha = 0.18F),
                backgroundColor = Color.White.copy(alpha = 0.08F),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.72F),
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .size(19.dp)
                    )
                },
                trailingIcon = {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.14F))
                            .clickable {
                                keyboardController?.hide()
                                state.onClearTextField()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
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
                placeholderText = stringResource(id = R.string.columns_placeholder),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(62.dp),
                textSize = 14.sp,
                textStyle = Poppins,
                textColor = Color.White,
                borderColor = Color.White.copy(alpha = 0.18F),
                backgroundColor = Color.White.copy(alpha = 0.08F),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StickerItem(sticker: Sticker, state: UpdateAlbumUIState, rowIndex: Int) {
    var showMenu by remember { mutableStateOf(false) }
    val cellShape = RoundedCornerShape(11.dp)
    val isOwned = sticker.found
    val repeatedCount = sticker.repeated
    val ownedBrush = Brush.verticalGradient(
        listOf(
            Color(0xFF0B5E8E),
            Color(0xFF07365D)
        )
    )
    val missingBrush = Brush.verticalGradient(
        listOf(
            if (rowIndex % 2 == 0) Color(0xFFF8FBFF) else Color(0xFFF1F6FA),
            Color(0xFFE7EEF5)
        )
    )
    val baseModifier = Modifier
        .aspectRatio(1.08F)
        .shadow(4.dp, cellShape, clip = false)
        .clip(cellShape)
        .background(if (isOwned) ownedBrush else missingBrush)
    val cardModifier = if (isOwned) {
        baseModifier.border(1.dp, Color(0xFF38A9E6).copy(alpha = 0.78F), cellShape)
    } else {
        baseModifier.border(1.dp, Color(0xFFD6E1EA), cellShape)
    }
    val contentColor = if (isOwned) Color.White else Color(0xFF647486)

    Box(
        modifier = cardModifier
            .combinedClickable(
                onClick = { state.onStickerClick(sticker) },
                onLongClick = { showMenu = true }
            )
    ) {
        Text(
            text = sticker.identifier,
            fontSize = 18.sp,
            modifier = Modifier
                .align(if (isOwned) Alignment.TopCenter else Alignment.Center)
                .padding(top = if (isOwned) 14.dp else 0.dp)
                .padding(horizontal = 2.dp),
            fontWeight = FontWeight.SemiBold,
            color = contentColor,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        if (isOwned) {
            StickerCountControls(
                repeatedCount = repeatedCount,
                onRemove = { state.onRemoveSticker(sticker) },
                onAdd = { state.onStickerClick(sticker) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 7.dp)
            )
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
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CopyActionPill(
            text = stringResource(id = R.string.copy_missing_stickers),
            onClick = { state.onCopyMissingStickersClick(context) },
            modifier = Modifier.weight(1F)
        )

        CopyActionPill(
            text = stringResource(id = R.string.copy_repeated_stickers),
            onClick = { state.onCopyRepeatedStickersClick(context) },
            modifier = Modifier.weight(1F)
        )
    }
}

@Composable
fun CopyActionPill(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.10F))
            .border(1.dp, Color.White.copy(alpha = 0.16F), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_copy),
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.78F),
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            style = Poppins
        )
    }
}

@Composable
private fun StickerCountControls(
    repeatedCount: Int,
    onRemove: () -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StickerCounterButton(
            icon = R.drawable.ic_remove,
            onClick = onRemove
        )

        Text(
            text = repeatedCount.toString(),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1F),
            style = Poppins
        )

        StickerCounterButton(
            icon = R.drawable.ic_add,
            onClick = onAdd
        )
    }
}

@Composable
private fun StickerCounterButton(
    icon: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.22F))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(17.dp)
        )
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
                    .background(Color(0xFF1D7BE0), CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        state.onReturnToTopButtonClick(lazyListState, scope)
                    }
                    .padding(6.dp),
                colorFilter = ColorFilter.tint(Color.White)
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
