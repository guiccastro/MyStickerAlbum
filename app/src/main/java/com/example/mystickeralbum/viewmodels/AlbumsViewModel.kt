package com.example.mystickeralbum.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystickeralbum.AlbumsRepository
import com.example.mystickeralbum.stateholders.AlbumsUIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumsViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<AlbumsUIState> =
        MutableStateFlow(AlbumsUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        updateAlbumsList()
    }

    fun updateAlbumsList() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    albumsList = withContext(IO) { AlbumsRepository.getAllAlbums() }
                )
            }
        }
    }
}