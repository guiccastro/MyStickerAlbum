package com.example.mystickeralbum.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystickeralbum.AlbumsRepository
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
                onReceivedAlbumName = ::onReceiveAlbumName
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
}