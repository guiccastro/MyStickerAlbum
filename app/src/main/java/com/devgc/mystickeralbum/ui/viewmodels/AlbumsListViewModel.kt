package com.devgc.mystickeralbum.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.devgc.mystickeralbum.AlbumsRepository
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.navigation.MainNavComponent
import com.devgc.mystickeralbum.navigation.NavigationParameters
import com.devgc.mystickeralbum.navigation.screens.CreateAlbumScreen
import com.devgc.mystickeralbum.navigation.screens.UpdateAlbumScreen
import com.devgc.mystickeralbum.ui.stateholders.AlbumsListUIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumsListViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<AlbumsListUIState> =
        MutableStateFlow(AlbumsListUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                onAlbumClick = ::onAlbumClick,
                changeIconsLegendDialogState = ::changeIconsLegendDialogState
            )
        }

        CoroutineScope(IO).launch {
            AlbumsRepository.getAllAlbumsFlow().collect { albumsList ->
                updateAlbumsList(albumsList)
            }
        }
    }

    private fun updateAlbumsList(albumsList: List<Album>) {
        _uiState.update {
            it.copy(
                albumsList = albumsList
            )
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
                navigateToItself(
                    parameters = NavigationParameters(
                        albumName = album.name
                    )
                )
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