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
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
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
import com.devgc.mystickeralbum.model.ToggleGroupValues
import com.devgc.mystickeralbum.ui.components.IconsLegendDialog
import com.devgc.mystickeralbum.ui.components.SimpleDialog
import com.devgc.mystickeralbum.ui.components.TextField
import com.devgc.mystickeralbum.ui.components.ToggleGroup
import com.devgc.mystickeralbum.ui.stateholders.UpdateAlbumUIState
import com.devgc.mystickeralbum.ui.stateholders.WorldCupQuickFilterOrder
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.devgc.mystickeralbum.ui.theme.Poppins
import com.devgc.mystickeralbum.ui.viewmodels.UpdateAlbumViewModel
import kotlinx.coroutines.CoroutineScope
import kotlin.math.roundToInt

private val ScreenBackgroundBrush = Brush.verticalGradient(
    listOf(
        Color(0xFF071827),
        Color(0xFF0B2D4A),
        Color(0xFF071420)
    )
)
private val AlbumCoverBrush = Brush.verticalGradient(
    listOf(
        Color(0xFF35A9E8),
        Color(0xFF16598F)
    )
)
private val StickerCellShape = RoundedCornerShape(11.dp)
private val OwnedStickerColor = Color(0xFF0B5E8E)
private val MissingStickerEvenColor = Color(0xFFF8FBFF)
private val MissingStickerOddColor = Color(0xFFF1F6FA)
private val OwnedStickerBorderColor = Color(0xFF38A9E6).copy(alpha = 0.78F)
private val MissingStickerBorderColor = Color(0xFFD6E1EA)
private const val WorldCup2026AlbumName = "Copa do Mundo 2026"
private const val MaxWorldCupTeamsPerAlphabeticalColumn = 4
private const val EnglandFlag = "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F"
private const val ScotlandFlag = "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC73\uDB40\uDC63\uDB40\uDC74\uDB40\uDC7F"

private data class WorldCupTeam(
    val code: String,
    val flag: String
)

private data class WorldCupTeamGroup(
    val title: String,
    val teams: List<WorldCupTeam>
)

private val WorldCup2026Teams = listOf(
    WorldCupTeam("ALG", "🇩🇿"),
    WorldCupTeam("ARG", "🇦🇷"),
    WorldCupTeam("AUS", "🇦🇺"),
    WorldCupTeam("AUT", "🇦🇹"),
    WorldCupTeam("BEL", "🇧🇪"),
    WorldCupTeam("BIH", "🇧🇦"),
    WorldCupTeam("BRA", "🇧🇷"),
    WorldCupTeam("CAN", "🇨🇦"),
    WorldCupTeam("CIV", "🇨🇮"),
    WorldCupTeam("COD", "🇨🇩"),
    WorldCupTeam("COL", "🇨🇴"),
    WorldCupTeam("CPV", "🇨🇻"),
    WorldCupTeam("CRO", "🇭🇷"),
    WorldCupTeam("CUW", "🇨🇼"),
    WorldCupTeam("CZE", "🇨🇿"),
    WorldCupTeam("ECU", "🇪🇨"),
    WorldCupTeam("EGY", "🇪🇬"),
    WorldCupTeam("ENG", EnglandFlag),
    WorldCupTeam("ESP", "🇪🇸"),
    WorldCupTeam("FRA", "🇫🇷"),
    WorldCupTeam("GER", "🇩🇪"),
    WorldCupTeam("GHA", "🇬🇭"),
    WorldCupTeam("HAI", "🇭🇹"),
    WorldCupTeam("IRN", "🇮🇷"),
    WorldCupTeam("IRQ", "🇮🇶"),
    WorldCupTeam("JOR", "🇯🇴"),
    WorldCupTeam("JPN", "🇯🇵"),
    WorldCupTeam("KOR", "🇰🇷"),
    WorldCupTeam("KSA", "🇸🇦"),
    WorldCupTeam("MAR", "🇲🇦"),
    WorldCupTeam("MEX", "🇲🇽"),
    WorldCupTeam("NED", "🇳🇱"),
    WorldCupTeam("NOR", "🇳🇴"),
    WorldCupTeam("NZL", "🇳🇿"),
    WorldCupTeam("PAN", "🇵🇦"),
    WorldCupTeam("PAR", "🇵🇾"),
    WorldCupTeam("POR", "🇵🇹"),
    WorldCupTeam("QAT", "🇶🇦"),
    WorldCupTeam("RSA", "🇿🇦"),
    WorldCupTeam("SCO", ScotlandFlag),
    WorldCupTeam("SEN", "🇸🇳"),
    WorldCupTeam("SUI", "🇨🇭"),
    WorldCupTeam("SWE", "🇸🇪"),
    WorldCupTeam("TUN", "🇹🇳"),
    WorldCupTeam("TUR", "🇹🇷"),
    WorldCupTeam("URU", "🇺🇾"),
    WorldCupTeam("USA", "🇺🇸"),
    WorldCupTeam("UZB", "🇺🇿")
)

private val WorldCup2026OfficialGroups = listOf(
    "A" to listOf("MEX", "RSA", "KOR", "CZE"),
    "B" to listOf("CAN", "BIH", "QAT", "SUI"),
    "C" to listOf("BRA", "MAR", "HAI", "SCO"),
    "D" to listOf("USA", "PAR", "AUS", "TUR"),
    "E" to listOf("GER", "CUW", "CIV", "ECU"),
    "F" to listOf("NED", "JPN", "SWE", "TUN"),
    "G" to listOf("BEL", "EGY", "IRN", "NZL"),
    "H" to listOf("ESP", "CPV", "KSA", "URU"),
    "I" to listOf("FRA", "SEN", "IRQ", "NOR"),
    "J" to listOf("ARG", "ALG", "AUT", "JOR"),
    "K" to listOf("POR", "COD", "UZB", "COL"),
    "L" to listOf("ENG", "CRO", "GHA", "PAN")
)

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
    val stickerRows = remember(state.stickers, columns) {
        state.stickers.toStickerRows(columns)
    }
    val gridSpacing = stickerGridSpacing(columns)
    val onStickerClick = state.onStickerClick
    val onRemoveSticker = state.onRemoveSticker
    val onToggleStickerLineBreak = state.onToggleStickerLineBreak
    val onToggleStickerExtraLine = state.onToggleStickerExtraLine

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackgroundBrush)
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
            verticalArrangement = Arrangement.spacedBy(gridSpacing),
            contentPadding = PaddingValues(top = 2.dp, bottom = 12.dp)
        ) {
            itemsIndexed(
                items = stickerRows,
                key = { rowIndex, row -> row.key(rowIndex) }
            ) { rowIndex, row ->
                when (row) {
                    StickerGridRow.Empty -> {
                        EmptyStickerRow(columns, gridSpacing)
                    }

                    is StickerGridRow.Stickers -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(gridSpacing)
                        ) {
                            row.stickers.forEachIndexed { columnIndex, sticker ->
                                key(sticker.identifier, columnIndex) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1F)
                                    ) {
                                        StickerItem(
                                            sticker = sticker,
                                            rowIndex = rowIndex,
                                            onStickerClick = onStickerClick,
                                            onRemoveSticker = onRemoveSticker,
                                            onToggleStickerLineBreak = onToggleStickerLineBreak,
                                            onToggleStickerExtraLine = onToggleStickerExtraLine
                                        )
                                    }
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

        ReturnToTopButton(
            showReturnToTopButton = state.showReturnToTopButton,
            lazyListState = lazyListState,
            onReturnToTopButtonClick = state.onReturnToTopButtonClick
        )
    }
}

private fun stickerGridSpacing(columns: Int): Dp {
    return when {
        columns <= 5 -> 8.dp
        columns <= 7 -> 6.dp
        columns <= 10 -> 4.dp
        columns <= 14 -> 3.dp
        else -> 2.dp
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
private fun EmptyStickerRow(columns: Int, gridSpacing: Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(gridSpacing)
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

private fun StickerGridRow.key(index: Int): String {
    return when (this) {
        StickerGridRow.Empty -> "empty-$index"
        is StickerGridRow.Stickers -> stickers.joinToString(
            separator = "|",
            prefix = "stickers-"
        ) { sticker ->
            "${sticker.identifier}:${sticker.lineBreakAfter}:${sticker.extraLineAfter}"
        }
    }
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
    val stickerStats = remember(state.album.stickersList.stickers) {
        state.album.stickersList.stickers.toStickerStats()
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        AlbumCompactSummary(state.album, stickerStats)
        SearchSticker(state, defaultColumns)
        WorldCup2026QuickFilter(state)
    }
}

private data class StickerStats(
    val total: Int,
    val found: Int,
    val missing: Int,
    val repeated: Int,
    val progress: Float,
    val formattedProgress: String
)

private fun List<Sticker>.toStickerStats(): StickerStats {
    var found = 0
    var repeated = 0

    forEach { sticker ->
        if (sticker.found) {
            found++
        }
        repeated += sticker.repeated
    }

    val total = size
    val progress = if (total == 0) 0F else found.toFloat() / total

    return StickerStats(
        total = total,
        found = found,
        missing = total - found,
        repeated = repeated,
        progress = progress,
        formattedProgress = "${(progress * 100).roundToInt()}%"
    )
}

@Composable
private fun AlbumCompactSummary(album: Album, stats: StickerStats) {
    val shape = RoundedCornerShape(14.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color.White.copy(alpha = 0.08F))
            .border(1.dp, Color.White.copy(alpha = 0.14F), shape)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AlbumCover(
            album = album,
            size = 54.dp,
            cornerRadius = 12.dp,
            initialsSize = 17.sp
        )

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = album.name,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1F),
                    style = Poppins
                )

                Text(
                    text = stats.formattedProgress,
                    color = Color(0xFF6EE7B7),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    style = Poppins
                )
            }

            ProgressTrack(progress = stats.progress)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CompactStickerStat(
                    icon = R.drawable.ic_stat_total,
                    value = stats.total.toString(),
                    color = Color.White
                )
                CompactStickerStat(
                    icon = R.drawable.ic_stat_owned,
                    value = stats.found.toString(),
                    color = Color(0xFF6EE7B7)
                )
                CompactStickerStat(
                    icon = R.drawable.ic_stat_missing,
                    value = stats.missing.toString(),
                    color = Color(0xFFFF7A88)
                )
                CompactStickerStat(
                    icon = R.drawable.ic_stat_repeated,
                    value = stats.repeated.toString(),
                    color = Color(0xFFFFD166)
                )
            }
        }
    }
}

@Composable
private fun ProgressTrack(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.16F))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0F, 1F))
                .fillMaxHeight()
                .background(Color(0xFF37D4B5))
        )
    }
}

@Composable
private fun CompactStickerStat(
    icon: Int,
    value: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            colorFilter = ColorFilter.tint(color)
        )

        Text(
            text = value,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = Poppins
        )
    }
}

@Composable
fun AlbumCover(
    album: Album,
    size: Dp = 76.dp,
    cornerRadius: Dp = 14.dp,
    initialsSize: TextUnit = 22.sp
) {
    val context = LocalContext.current
    val imageState = ImageCropperHelper.imageStateHolder.collectAsState().value
    val bitmap = imageState.getImageState(context, album.albumImage)
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = Modifier
            .size(size)
            .shadow(8.dp, shape)
            .clip(shape)
            .background(AlbumCoverBrush),
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
                fontSize = initialsSize,
                fontWeight = FontWeight.Bold,
                style = Poppins
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchSticker(state: UpdateAlbumUIState, defaultColumns: Int) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var columnsText by rememberSaveable(state.album.name, defaultColumns) {
        mutableStateOf((state.columns ?: defaultColumns).toString())
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (state.album.name == WorldCup2026AlbumName) {
            WorldCupQuickFilterToggle(state)
        }

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
                text = columnsText,
                onValueChange = { value ->
                    columnsText = value.filter { it.isDigit() }
                    state.onColumnsChanged(columnsText.toIntOrNull())
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

@Composable
private fun WorldCupQuickFilterToggle(state: UpdateAlbumUIState) {
    val selectedOrder = state.worldCupQuickFilterOrder

    ToggleGroup(
        toggleGroupValues = ToggleGroupValues(
            options = listOf(
                stringResource(id = R.string.world_cup_quick_filter_groups),
                stringResource(id = R.string.world_cup_quick_filter_alphabetical)
            ),
            selectedIndex = selectedOrder.ordinal,
            onOptionClick = { index ->
                state.onWorldCupQuickFilterOrderSelected(
                    WorldCupQuickFilterOrder.getByIndex(index)
                )
            }
        ),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        selectedFontWeight = FontWeight.Bold,
        cornerRadius = 12.dp,
        selectedBackground = Color(0xFF0B5E8E),
        selectedColorBorder = Color(0xFF6EE7B7)
    )
}

@Composable
private fun WorldCup2026QuickFilter(state: UpdateAlbumUIState) {
    if (state.album.name != WorldCup2026AlbumName) {
        return
    }

    val selectedOrder = state.worldCupQuickFilterOrder
    val teamGroups = remember(selectedOrder) {
        when (selectedOrder) {
            WorldCupQuickFilterOrder.Groups -> WorldCup2026Teams.toOfficialGroups()
            WorldCupQuickFilterOrder.Alphabetical -> WorldCup2026Teams.toAlphabeticalGroups()
        }
    }
    val selectedCode = state.searchStickerTextField.text.trim()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        itemsIndexed(
            items = teamGroups,
            key = { index, group -> "${group.title}-$index" }
        ) { _, group ->
            WorldCupTeamColumn(
                group = group,
                selectedCode = selectedCode,
                onClick = state.onWorldCupTeamSelected
            )
        }
    }
}

private fun List<WorldCupTeam>.toOfficialGroups(): List<WorldCupTeamGroup> {
    val teamsByCode = associateBy { team -> team.code }

    return WorldCup2026OfficialGroups.map { (title, codes) ->
        WorldCupTeamGroup(
            title = title,
            teams = codes.map { code -> teamsByCode.getValue(code) }
        )
    }
}

private fun List<WorldCupTeam>.toAlphabeticalGroups(): List<WorldCupTeamGroup> {
    val teamsByTitle = linkedMapOf<String, ArrayList<WorldCupTeam>>()

    sortedBy { team -> team.code }.forEach { team ->
        val title = team.code.first().toString()
        teamsByTitle.getOrPut(title) { ArrayList() }.add(team)
    }

    return teamsByTitle.flatMap { (title, teams) ->
        teams.chunked(MaxWorldCupTeamsPerAlphabeticalColumn).map { chunk ->
            WorldCupTeamGroup(
                title = title,
                teams = chunk
            )
        }
    }
}

@Composable
private fun WorldCupTeamColumn(
    group: WorldCupTeamGroup,
    selectedCode: String,
    onClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        WorldCupGroupHeader(group.title)

        group.teams.forEach { team ->
            WorldCupTeamPill(
                team = team,
                selected = selectedCode.equals(team.code, ignoreCase = true),
                onClick = onClick
            )
        }
    }
}

@Composable
private fun WorldCupGroupHeader(title: String) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.12F))
            .border(1.dp, Color.White.copy(alpha = 0.16F), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.78F),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            style = Poppins
        )
    }
}

@Composable
private fun WorldCupTeamPill(
    team: WorldCupTeam,
    selected: Boolean,
    onClick: (String) -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val backgroundColor = if (selected) {
        Color(0xFF37D4B5).copy(alpha = 0.26F)
    } else {
        Color.White.copy(alpha = 0.09F)
    }
    val borderColor = if (selected) {
        Color(0xFF6EE7B7)
    } else {
        Color.White.copy(alpha = 0.16F)
    }

    Row(
        modifier = Modifier
            .height(40.dp)
            .clip(shape)
            .background(backgroundColor)
            .border(1.dp, borderColor, shape)
            .clickable { onClick(team.code) }
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = team.flag,
            color = Color.White,
            fontSize = 18.sp,
            maxLines = 1,
            style = Poppins
        )

        Text(
            text = team.code,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            style = Poppins
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StickerItem(
    sticker: Sticker,
    rowIndex: Int,
    onStickerClick: (Sticker) -> Unit,
    onRemoveSticker: (Sticker) -> Unit,
    onToggleStickerLineBreak: (Sticker) -> Unit,
    onToggleStickerExtraLine: (Sticker) -> Unit
) {
    var showMenu by remember(sticker.identifier) { mutableStateOf(false) }
    val isOwned = sticker.found
    val repeatedCount = sticker.repeated
    val backgroundColor = if (isOwned) {
        OwnedStickerColor
    } else if (rowIndex % 2 == 0) {
        MissingStickerEvenColor
    } else {
        MissingStickerOddColor
    }
    val borderColor = if (isOwned) {
        OwnedStickerBorderColor
    } else {
        MissingStickerBorderColor
    }
    val contentColor = if (isOwned) Color.White else Color(0xFF647486)

    BoxWithConstraints(
        modifier = Modifier
            .aspectRatio(1.08F)
            .clip(StickerCellShape)
            .background(backgroundColor)
            .border(1.dp, borderColor, StickerCellShape)
            .combinedClickable(
                onClick = { onStickerClick(sticker) },
                onLongClick = { showMenu = true }
            )
    ) {
        val identifierTextSize = when {
            maxWidth < 40.dp -> 8.sp
            maxWidth < 50.dp -> 10.sp
            maxWidth < 64.dp -> 12.sp
            maxWidth < 76.dp -> 14.sp
            else -> 18.sp
        }
        val ownedIdentifierTopPadding = when {
            maxWidth < 40.dp -> 0.dp
            maxWidth < 50.dp -> 1.dp
            maxWidth < 64.dp -> 2.dp
            maxWidth < 76.dp -> 4.dp
            else -> 8.dp
        }
        val controlButtonSize = when {
            maxWidth < 40.dp -> 10.dp
            maxWidth < 50.dp -> 12.dp
            maxWidth < 64.dp -> 14.dp
            maxWidth < 76.dp -> 16.dp
            else -> 20.dp
        }
        val controlIconSize = when {
            maxWidth < 40.dp -> 6.dp
            maxWidth < 50.dp -> 7.dp
            maxWidth < 64.dp -> 9.dp
            maxWidth < 76.dp -> 10.dp
            else -> 12.dp
        }
        val repeatedTextSize = when {
            maxWidth < 40.dp -> 7.sp
            maxWidth < 50.dp -> 8.sp
            maxWidth < 64.dp -> 9.sp
            maxWidth < 76.dp -> 11.sp
            else -> 13.sp
        }
        val controlsHorizontalPadding = when {
            maxWidth < 40.dp -> 1.dp
            maxWidth < 50.dp -> 2.dp
            maxWidth < 64.dp -> 3.dp
            else -> 5.dp
        }
        val controlsVerticalPadding = when {
            maxWidth < 40.dp -> 1.dp
            maxWidth < 50.dp -> 2.dp
            maxWidth < 64.dp -> 3.dp
            else -> 5.dp
        }

        if (isOwned) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = controlsHorizontalPadding,
                        vertical = controlsVerticalPadding
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sticker.identifier,
                    fontSize = identifierTextSize,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = ownedIdentifierTopPadding),
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    lineHeight = identifierTextSize
                )

                StickerCountControls(
                    repeatedCount = repeatedCount,
                    onRemove = { onRemoveSticker(sticker) },
                    onAdd = { onStickerClick(sticker) },
                    modifier = Modifier.fillMaxWidth(),
                    buttonSize = controlButtonSize,
                    iconSize = controlIconSize,
                    textSize = repeatedTextSize
                )
            }
        } else {
            Text(
                text = sticker.identifier,
                fontSize = identifierTextSize,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 2.dp),
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
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
                    onToggleStickerLineBreak(sticker)
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
                    onToggleStickerExtraLine(sticker)
                }
            )
        }
    }
}

@Composable
private fun StickerCountControls(
    repeatedCount: Int,
    onRemove: () -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp,
    iconSize: Dp,
    textSize: TextUnit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StickerCounterButton(
            icon = R.drawable.ic_remove,
            onClick = onRemove,
            buttonSize = buttonSize,
            iconSize = iconSize
        )

        Text(
            text = repeatedCount.toString(),
            color = Color.White,
            fontSize = textSize,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1F),
            style = Poppins
        )

        StickerCounterButton(
            icon = R.drawable.ic_add,
            onClick = onAdd,
            buttonSize = buttonSize,
            iconSize = iconSize
        )
    }
}

@Composable
private fun StickerCounterButton(
    icon: Int,
    onClick: () -> Unit,
    buttonSize: Dp,
    iconSize: Dp
) {
    Box(
        modifier = Modifier
            .size(buttonSize)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.22F))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun ReturnToTopButton(
    showReturnToTopButton: Boolean,
    lazyListState: LazyListState,
    onReturnToTopButtonClick: (LazyListState, CoroutineScope) -> Unit
) {
    val scope = rememberCoroutineScope()
    AnimatedVisibility(
        visible = showReturnToTopButton,
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
                        onReturnToTopButtonClick(lazyListState, scope)
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
