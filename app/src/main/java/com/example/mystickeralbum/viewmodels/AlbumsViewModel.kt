package com.example.mystickeralbum.viewmodels

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                onFabClick = ::onFabClick
            )
        }

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

    private fun onFabClick(activity: Activity) {
        activity.apply {
            val intent = Intent(this, AddAlbumActivity::class.java)
            startActivity(intent)
        }
    }
}