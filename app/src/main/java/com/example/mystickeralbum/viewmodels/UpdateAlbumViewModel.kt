package com.example.mystickeralbum.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mystickeralbum.stateholders.UpdateAlbumUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UpdateAlbumViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<UpdateAlbumUIState> =
        MutableStateFlow(UpdateAlbumUIState())
    val uiState get() = _uiState.asStateFlow()
}