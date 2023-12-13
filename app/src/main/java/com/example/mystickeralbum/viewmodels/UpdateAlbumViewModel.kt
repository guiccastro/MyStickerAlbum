package com.example.mystickeralbum.viewmodels

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystickeralbum.AlbumsRepository
import com.example.mystickeralbum.R
import com.example.mystickeralbum.activities.CreateEditAlbumActivity
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

    companion object {
        const val ALBUM_NAME_EXTRA = "ALBUM_NAME"
        val REQUEST_CODE = this.hashCode()
    }

    init {
        _uiState.update {
            it.copy(
                onReceivedAlbumName = ::onReceiveAlbumName,
                onStickerClick = ::onStickerClick,
                onCloseStickerDialog = ::onCloseDialog,
                onFoundNotFoundClick = ::onFoundNotFoundClick,
                onChangeRepeatedStickerClick = ::onChangeRepeatedStickerClick,
                onDeleteAlbumClick = ::onDeleteAlbumClick,
                onCloseDeleteAlbumDialog = ::onCloseDeleteAlbumDialog,
                onConfirmDeleteAlbumDialog = ::onConfirmDeleteAlbumDialog,
                onEditAlbumClick = ::onEditAlbumClick,
                onCopyMissingStickersClick = ::onCopyMissingStickersClick,
                onCopyRepeatedStickersClick = ::onCopyRepeatedStickersClick
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

    private fun onDeleteAlbumClick() {
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

    private fun onConfirmDeleteAlbumDialog(activity: Activity) {
        viewModelScope.launch {
            withContext(IO) {
                AlbumsRepository.removeAlbum(_uiState.value.album)
            }

            activity.finish()
        }
    }

    private fun onEditAlbumClick(activity: Activity) {
        activity.apply {
            val intent = Intent(this, CreateEditAlbumActivity::class.java)
            intent.putExtra(CreateEditAlbumViewModel.ALBUM_NAME_EXTRA, _uiState.value.album.name)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val albumName = data?.getStringExtra(ALBUM_NAME_EXTRA) ?: ""
                Log.println(Log.ASSERT, "albumName", albumName)
                onReceiveAlbumName(albumName)
            }
        }
    }

    private fun copyTextToClipboard(activity: Activity, text: String) {
        val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Label", text)
        clipboard.setPrimaryClip(clip)
    }

    private fun onCopyMissingStickersClick(activity: Activity) {
        copyTextToClipboard(activity, getMissingStickersText())
        Toast.makeText(activity, R.string.message_missing_stickers_copied, Toast.LENGTH_SHORT)
            .show()
    }

    private fun onCopyRepeatedStickersClick(activity: Activity) {
        copyTextToClipboard(activity, getRepeatedStickersText())
        Toast.makeText(activity, R.string.message_repeated_stickers_copied, Toast.LENGTH_SHORT)
            .show()
    }

    private fun getMissingStickersText(): String {
        return _uiState.value.album.getMissing().joinToString(" - ") { it.identifier }
    }

    private fun getRepeatedStickersText(): String {
        return _uiState.value.album.getRepeated().joinToString(" - ") { it.identifier }
    }
}