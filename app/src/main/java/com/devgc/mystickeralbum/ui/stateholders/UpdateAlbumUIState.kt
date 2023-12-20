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
    val onCloseStickerDialog: () -> Unit = {},
    val stickerDialog: Sticker = Sticker("", false, 0),
    val showStickerDialog: Boolean = false,
    val onFoundNotFoundClick: (Boolean) -> Unit = {},
    val onChangeRepeatedStickerClick: (Int) -> Unit = {},
    val showDeleteAlbumDialog: Boolean = false,
    val onCloseDeleteAlbumDialog: () -> Unit = {},
    val onConfirmDeleteAlbumDialog: () -> Unit = {},
    val onCopyMissingStickersClick: (Context) -> Unit = {},
    val onCopyRepeatedStickersClick: (Context) -> Unit = {},
    val changeIconsLegendDialogState: () -> Unit = {},
    val showIconsLegendDialog: Boolean = false,
    val showSearchStickerTextField: Boolean = false,
    val searchStickerTextField: TextFieldValues = TextFieldValues(),
    val onSearchStickerClick: (LazyListState, CoroutineScope) -> Unit = { _, _ -> },
    val onScroll: (Int) -> Unit = {},
    val showReturnToTopButton: Boolean = false,
    val onReturnToTopButtonClick: (LazyListState, CoroutineScope) -> Unit = { _, _ -> }
)
