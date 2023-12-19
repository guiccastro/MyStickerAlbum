package com.devgc.mystickeralbum.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devgc.mystickeralbum.AlbumsRepository
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.navigation.MainNavComponent
import com.devgc.mystickeralbum.navigation.screens.CreateAlbumScreen
import com.devgc.mystickeralbum.navigation.screens.UpdateAlbumScreen
import com.devgc.mystickeralbum.ui.stateholders.AlbumsListUIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumsListViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<AlbumsListUIState> =
        MutableStateFlow(AlbumsListUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                onAlbumClick = ::onAlbumClick,
                updateAlbumsList = ::updateAlbumsList,
                changeIconsLegendDialogState = ::changeIconsLegendDialogState
            )
        }
    }

    private fun updateAlbumsList() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    albumsList = withContext(IO) {
                        AlbumsRepository.getAllAlbums().reversed()
                    }
                )
            }
        }
    }

    fun onFabClick() {
        MainNavComponent.navController.apply {
            CreateAlbumScreen.apply {
                navigateToItself()
            }
        }
    }

    private fun onAlbumClick(album: Album) {
        MainNavComponent.navController.apply {
            UpdateAlbumScreen.apply {
                navigateToItself(albumName = album.name)
            }
        }
    }

    fun changeIconsLegendDialogState() {
        _uiState.update {
            it.copy(
                showIconsLegendDialog = !_uiState.value.showIconsLegendDialog
            )
        }
    }
}