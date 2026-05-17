package com.devgc.mystickeralbum.ui.viewmodels

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.devgc.mystickeralbum.AlbumsRepository
import com.devgc.mystickeralbum.MyStickerAlbumApplication
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.ImageCropperHelper
import com.devgc.mystickeralbum.navigation.MainNavComponent
import com.devgc.mystickeralbum.ui.stateholders.CropImageUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CropImageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<CropImageUIState> =
        MutableStateFlow(CropImageUIState())
    val uiState get() = _uiState.asStateFlow()

    private var album: Album? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            savedStateHandle
                .getStateFlow<String?>(MainNavComponent.albumNameArgument, null)
                .filterNotNull()
                .collect { albumName ->
                    AlbumsRepository.getAlbumByNameFlow(albumName).collect { album ->
                        album?.let {
                            onReceivedAlbum(it)
                        }
                    }
                }
        }

        CoroutineScope(Dispatchers.IO).launch {
            savedStateHandle
                .getStateFlow<Float?>(MainNavComponent.aspectRatioArgument, null)
                .filterNotNull()
                .collect { aspectRatio ->
                    onReceivedAspectRatio(aspectRatio)
                }
        }

        _uiState.update {
            it.copy(
                onCropSuccess = ::onCropSuccess
            )
        }
    }

    private fun onReceivedAlbum(album: Album) {
        this.album = album

        val context = MyStickerAlbumApplication.getInstance()
        _uiState.update {
            it.copy(
                imageBitmap = ImageCropperHelper.getOriginalImage(context, album.albumImage)
                    ?.asImageBitmap()
            )
        }
    }

    private fun onReceivedAspectRatio(aspectRatio: Float) {
        _uiState.update {
            it.copy(
                aspectRatio = aspectRatio
            )
        }
    }

    private fun onCropSuccess(imageBitmap: ImageBitmap) {
        val context = MyStickerAlbumApplication.getInstance()

        album?.albumImage?.let { key ->
            CoroutineScope(Dispatchers.IO).launch {
                ImageCropperHelper.saveImage(
                    context,
                    imageBitmap.asAndroidBitmap(),
                    key
                )

                withContext(Main) {
                    MainNavComponent.navController.popBackStack()
                }
            }
        }
    }
}