package com.devgc.mystickeralbum.ui.viewmodels

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devgc.mystickeralbum.AlbumsRepository
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.StickersList
import com.devgc.mystickeralbum.navigation.MainNavComponent.Companion.albumNameArgument
import com.devgc.mystickeralbum.navigation.MainNavComponent.Companion.navController
import com.devgc.mystickeralbum.navigation.screens.EditAlbumScreen
import com.devgc.mystickeralbum.ui.stateholders.UpdateAlbumUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UpdateAlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<UpdateAlbumUIState> =
        MutableStateFlow(UpdateAlbumUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                onStickerClick = ::onStickerClick,
                onCloseStickerDialog = ::onCloseDialog,
                onFoundNotFoundClick = ::onFoundNotFoundClick,
                onChangeRepeatedStickerClick = ::onChangeRepeatedStickerClick,
                onCloseDeleteAlbumDialog = ::onCloseDeleteAlbumDialog,
                onConfirmDeleteAlbumDialog = ::onConfirmDeleteAlbumDialog,
                onCopyMissingStickersClick = ::onCopyMissingStickersClick,
                onCopyRepeatedStickersClick = ::onCopyRepeatedStickersClick
            )
        }

        CoroutineScope(IO).launch {
            savedStateHandle
                .getStateFlow<String?>(albumNameArgument, null)
                .filterNotNull()
                .collect { albumName ->
                    onReceiveAlbumName(albumName)
                }
        }
    }

    private fun onReceiveAlbumName(albumName: String) {
        viewModelScope.launch {
            val album = withContext(IO) {
                return@withContext AlbumsRepository.getAlbumByName(albumName)
            } ?: return@launch

            _uiState.update {
                it.copy(
                    album = album
                )
            }
        }
    }

    private fun onStickerClick(sticker: Sticker) {
        _uiState.update {
            it.copy(
                showStickerDialog = true,
                stickerDialog = sticker
            )
        }
    }

    private fun onCloseDialog() {
        _uiState.update {
            it.copy(
                showStickerDialog = false
            )
        }
    }

    private fun onFoundNotFoundClick(found: Boolean) {
        val newSticker = _uiState.value.stickerDialog.copy(found = found, repeated = 0)
        updateSticker(newSticker)
        onCloseDialog()
    }

    private fun onChangeRepeatedStickerClick(value: Int) {
        val newRepeated = _uiState.value.stickerDialog.repeated + value

        if (newRepeated >= 0) {
            val newSticker = _uiState.value.stickerDialog.copy(repeated = newRepeated)
            updateSticker(newSticker)
        }
    }

    private fun updateSticker(newSticker: Sticker) {
        val newStickers = ArrayList(_uiState.value.album.stickersList.stickers)

        newStickers.replaceAll {
            if (it.identifier == newSticker.identifier) {
                newSticker
            } else {
                it
            }
        }

        val newAlbum = _uiState.value.album.copy(
            stickersList = StickersList(newStickers)
        )

        viewModelScope.launch {
            withContext(IO) {
                AlbumsRepository.updateStickers(newAlbum)
            }
        }

        _uiState.update {
            it.copy(
                album = newAlbum,
                stickerDialog = newSticker
            )
        }
    }

    fun onDeleteAlbumClick() {
        _uiState.update {
            it.copy(
                showDeleteAlbumDialog = true
            )
        }
    }

    private fun onCloseDeleteAlbumDialog() {
        _uiState.update {
            it.copy(
                showDeleteAlbumDialog = false
            )
        }
    }

    private fun onConfirmDeleteAlbumDialog() {
        viewModelScope.launch {
            withContext(IO) {
                AlbumsRepository.removeAlbum(_uiState.value.album)
            }

            navController.popBackStack()
        }
    }

    fun onEditAlbumClick() {
        navController.apply {
            EditAlbumScreen.apply {
                navigateToItself(albumName = _uiState.value.album.name)
            }
        }
    }

    private fun copyTextToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Label", text)
        clipboard.setPrimaryClip(clip)
    }

    private fun onCopyMissingStickersClick(context: Context) {
        copyTextToClipboard(context, getMissingStickersText())
        Toast.makeText(context, R.string.message_missing_stickers_copied, Toast.LENGTH_SHORT)
            .show()
    }

    private fun onCopyRepeatedStickersClick(context: Context) {
        copyTextToClipboard(context, getRepeatedStickersText())
        Toast.makeText(context, R.string.message_repeated_stickers_copied, Toast.LENGTH_SHORT)
            .show()
    }

    private fun getMissingStickersText(): String {
        return _uiState.value.album.getMissing().joinToString(" - ") { it.identifier }
    }

    private fun getRepeatedStickersText(): String {
        return _uiState.value.album.getRepeated().joinToString(" - ") { it.identifier }
    }
}