package com.example.mystickeralbum.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.SpecialStickerType
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.stateholders.AddAlbumUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddAlbumViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<AddAlbumUIState> =
        MutableStateFlow(AddAlbumUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                onAlbumNameChange = ::onAlbumNameChange,
                onAlbumImageUrlChange = ::onAlbumImageUrlChange,
                onNormalStickersFromChange = ::onNormalStickersFromChange,
                onNormalStickersToChange = ::onNormalStickersToChange,
                onHasSpecialStickersChange = ::onHasSpecialStickersChange,
                onSpecialStickerTypeChange = ::onSpecialStickerTypeChange,
                onSpecialStickersLetterFromChange = ::onSpecialStickersLetterFromChange,
                onSpecialStickersLetterToChange = ::onSpecialStickersLetterToChange,
                onSpecialStickersNumberFromChange = ::onSpecialStickersNumberFromChange,
                onSpecialStickersNumberToChange = ::onSpecialStickersNumberToChange,
                onCreateClick = ::onCreateClick
            )
        }
    }

    private fun onAlbumNameChange(name: String) {
        _uiState.update {
            it.copy(
                albumName = name
            )
        }
    }

    private fun onAlbumImageUrlChange(url: String) {
        _uiState.update {
            it.copy(
                albumImageUrl = url
            )
        }
    }

    private fun onNormalStickersFromChange(from: String) {
        _uiState.update {
            it.copy(
                normalStickersFrom = from.toInt()
            )
        }
    }

    private fun onNormalStickersToChange(to: String) {
        _uiState.update {
            it.copy(
                normalStickersTo = to.toInt()
            )
        }
    }

    private fun onHasSpecialStickersChange(hasSpecialStickers: Boolean) {
        _uiState.update {
            it.copy(
                hasSpecialStickers = hasSpecialStickers
            )
        }
    }

    private fun onSpecialStickerTypeChange(specialStickerType: SpecialStickerType) {
        _uiState.update {
            it.copy(
                specialStickerType = specialStickerType
            )
        }
    }

    private fun onSpecialStickersLetterFromChange(letterFrom: String) {
        _uiState.update {
            it.copy(
                specialStickersLetterFrom = letterFrom
            )
        }
    }

    private fun onSpecialStickersLetterToChange(letterTo: String) {
        _uiState.update {
            it.copy(
                specialStickersLetterTo = letterTo
            )
        }
    }

    private fun onSpecialStickersNumberFromChange(numberFrom: String) {
        _uiState.update {
            it.copy(
                specialStickersNumberFrom = numberFrom.toInt()
            )
        }
    }

    private fun onSpecialStickersNumberToChange(numberTo: String) {
        _uiState.update {
            it.copy(
                specialStickersNumberTo = numberTo.toInt()
            )
        }
    }

    private fun onCreateClick() {
        val album = Album(
            name = _uiState.value.albumName,
            stickers = createStickersList(),
            status = AlbumStatus.Completing,
            albumImage = _uiState.value.albumImageUrl
        )

        Log.println(Log.ASSERT, "Album", album.stickers.map { it.identifier }.toString())
    }

    private fun createStickersList(): List<Sticker> {
        val stickers = ArrayList<Sticker>()
        stickers.addAll(createNormalStickersList())
        stickers.addAll(createSpecialStickersList())
        return stickers
    }

    private fun createNormalStickersList(): List<Sticker> {
        return (_uiState.value.normalStickersFrom.._uiState.value.normalStickersTo)
            .map {
                Sticker(
                    identifier = it.toString(),
                    found = false,
                    repeated = 0
                )
            }
    }

    private fun createSpecialStickersList(): List<Sticker> {
        val stickers = ArrayList<Sticker>()
        if (_uiState.value.hasSpecialStickers) {
            (_uiState.value.specialStickersLetterFrom.uppercase()
                .single().._uiState.value.specialStickersLetterTo.uppercase()
                .single()).forEach { letter ->
                stickers.addAll(
                    (_uiState.value.specialStickersNumberFrom.._uiState.value.specialStickersNumberTo).map { number ->
                        if (_uiState.value.specialStickerType == SpecialStickerType.LetterNumber) {
                            Sticker(
                                identifier = "$letter$number",
                                found = false,
                                repeated = 0
                            )
                        } else {
                            Sticker(
                                identifier = "$number$letter",
                                found = false,
                                repeated = 0
                            )
                        }
                    }
                )
            }
        }

        return stickers
    }
}