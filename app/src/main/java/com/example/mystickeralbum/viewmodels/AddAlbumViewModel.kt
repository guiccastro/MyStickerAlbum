package com.example.mystickeralbum.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.SpecialStickerType
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.onlyLetters
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
        if (from.toIntOrNull() != null || from.isEmpty()) {
            _uiState.update {
                it.copy(
                    normalStickersFrom = from
                )
            }
        }
    }

    private fun onNormalStickersToChange(to: String) {
        if (to.toIntOrNull() != null || to.isEmpty()) {
            _uiState.update {
                it.copy(
                    normalStickersTo = to
                )
            }
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
        val from = letterFrom.uppercase()
        if (from.onlyLetters()) {
            _uiState.update {
                it.copy(
                    specialStickersLetterFrom = from
                )
            }
        }
    }

    private fun onSpecialStickersLetterToChange(letterTo: String) {
        val from = letterTo.uppercase()
        if (from.onlyLetters()) {
            _uiState.update {
                it.copy(
                    specialStickersLetterTo = letterTo
                )
            }
        }
    }

    private fun onSpecialStickersNumberFromChange(numberFrom: String) {
        if (numberFrom.toIntOrNull() != null || numberFrom.isEmpty()) {
            _uiState.update {
                it.copy(
                    specialStickersNumberFrom = numberFrom
                )
            }
        }
    }

    private fun onSpecialStickersNumberToChange(numberTo: String) {
        if (numberTo.toIntOrNull() != null || numberTo.isEmpty()) {
            _uiState.update {
                it.copy(
                    specialStickersNumberTo = numberTo
                )
            }
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
        return (_uiState.value.normalStickersFrom.toInt().._uiState.value.normalStickersTo.toInt())
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
                    (_uiState.value.specialStickersNumberFrom.toInt().._uiState.value.specialStickersNumberTo.toInt()).map { number ->
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