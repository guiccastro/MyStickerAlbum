package com.example.mystickeralbum.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystickeralbum.AlbumsRepository
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.model.StickersList
import com.example.mystickeralbum.stateholders.UpdateAlbumUIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateAlbumViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<UpdateAlbumUIState> =
        MutableStateFlow(UpdateAlbumUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                onReceivedAlbumName = ::onReceiveAlbumName,
                onStickerClick = ::onStickerClick,
                onFoundNotFoundClick = ::onFoundNotFoundClick,
                onChangeRepeatedStickerClick = ::onChangeRepeatedStickerClick
            )
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
                showDialog = true,
                stickerDialog = sticker
            )
        }
    }

    private fun onFoundNotFoundClick(found: Boolean) {
        val newSticker = _uiState.value.stickerDialog.copy(found = found, repeated = 0)
        updateSticker(newSticker)
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
                AlbumsRepository.updateAlbum(newAlbum)
            }
        }

        _uiState.update {
            it.copy(
                album = newAlbum,
                stickerDialog = newSticker
            )
        }
    }
}