package com.devgc.mystickeralbum.ui.stateholders

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.AlbumStatus
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.StickersList
import com.devgc.mystickeralbum.model.TextFieldValues
import kotlinx.coroutines.CoroutineScope

data class UpdateAlbumUIState(
    val album: Album = Album("", StickersList(emptyList()), AlbumStatus.Completing, ""),
    val onStickerClick: (Sticker) -> Unit = {},
    val onRemoveSticker: (Sticker) -> Unit = {},
    val showDeleteAlbumDialog: Boolean = false,
    val onCloseDeleteAlbumDialog: () -> Unit = {},
    val onConfirmDeleteAlbumDialog: () -> Unit = {},
    val onCopyMissingStickersClick: (Context) -> Unit = {},
    val onCopyRepeatedStickersClick: (Context) -> Unit = {},
    val changeIconsLegendDialogState: () -> Unit = {},
    val showIconsLegendDialog: Boolean = false,
    val searchStickerTextField: TextFieldValues = TextFieldValues(),
    val onSearchStickerClick: (LazyListState, CoroutineScope) -> Unit = { _, _ -> },
    val onClearTextField: () -> Unit = {},
    val onScroll: (Int) -> Unit = {},
    val showReturnToTopButton: Boolean = false,
    val onReturnToTopButtonClick: (LazyListState, CoroutineScope) -> Unit = { _, _ -> },
    val columns: Int? = null,
    val onColumnsChanged: (Int?) -> Unit = {},
    val stickers: List<Sticker> = emptyList(),
    val onViewAll: () -> Unit = {},
    val onViewMissing: () -> Unit = {},
    val onViewRepeated: () -> Unit = {},
    val isHeaderVisible: Boolean = true,
    val onToggleHeader: () -> Unit = {}
)
