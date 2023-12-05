package com.example.mystickeralbum.viewmodels

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystickeralbum.model.AlbumItem
import com.example.mystickeralbum.AlbumsRepository
import com.example.mystickeralbum.activities.AddAlbumActivity
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
        _uiState.update {
            it.copy(
                onFabClick = ::onFabClick,
                onAlbumLongClick = ::onAlbumLongClick,
                onDeleteClick = ::onDeleteClick,
                onCloseEditModeClick = ::onCloseEditModeClick
            )
        }

        updateAlbumsList()
    }

    fun updateAlbumsList() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    albumsList = withContext(IO) {
                        AlbumsRepository.getAllAlbums().map { album -> AlbumItem(album, false) }
                    }
                )
            }
        }
    }

    private fun onFabClick(activity: Activity) {
        activity.apply {
            val intent = Intent(this, AddAlbumActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onAlbumLongClick(albumItem: AlbumItem) {
        val newList = _uiState.value.albumsList.map {
            if (albumItem == it) {
                it.copy(editMode = true)
            } else {
                it
            }
        }

        _uiState.update {
            it.copy(
                albumsList = newList
            )
        }
    }

    private fun onDeleteClick(albumItem: AlbumItem) {
        viewModelScope.launch {
            withContext(IO) {
                AlbumsRepository.removeAlbum(albumItem.album)
            }

            updateAlbumsList()
        }
    }

    private fun onCloseEditModeClick(albumItem: AlbumItem) {
        val newList = _uiState.value.albumsList.map {
            if (albumItem == it) {
                it.copy(editMode = false)
            } else {
                it
            }
        }

        _uiState.update {
            it.copy(
                albumsList = newList
            )
        }
    }
}