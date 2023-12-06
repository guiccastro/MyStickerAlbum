package com.example.mystickeralbum.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystickeralbum.AlbumsRepository
import com.example.mystickeralbum.R
import com.example.mystickeralbum.extensions.onlyLetters
import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.SpecialStickerType
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.model.StickersList
import com.example.mystickeralbum.model.TextFieldValues
import com.example.mystickeralbum.stateholders.CreateEditAlbumUIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateEditAlbumViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<CreateEditAlbumUIState> =
        MutableStateFlow(CreateEditAlbumUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                albumNameTextField = TextFieldValues(onTextChange = ::onAlbumNameChange),
                albumImageUrlTextField = TextFieldValues(onTextChange = ::onAlbumImageUrlChange),
                normalStickersFromTextField = TextFieldValues(onTextChange = ::onNormalStickersFromChange),
                normalStickersToTextField = TextFieldValues(onTextChange = ::onNormalStickersToChange),
                onHasSpecialStickersChange = ::onHasSpecialStickersChange,
                onSpecialStickerTypeChange = ::onSpecialStickerTypeChange,
                specialStickersLetterFromTextField = TextFieldValues(onTextChange = ::onSpecialStickersLetterFromChange),
                specialStickersLetterToTextField = TextFieldValues(onTextChange = ::onSpecialStickersLetterToChange),
                specialStickersNumberFromTextField = TextFieldValues(onTextChange = ::onSpecialStickersNumberFromChange),
                specialStickersNumberToTextField = TextFieldValues(onTextChange = ::onSpecialStickersNumberToChange),
                onCreateClick = ::onCreateClick,
                onReceivedAlbumName = ::onReceivedAlbumName
            )
        }
    }

    companion object {
        const val ALBUM_NAME_EXTRA = "ALBUM_NAME"
    }

    private fun onAlbumNameChange(name: String) {
        _uiState.update {
            it.copy(
                albumNameTextField = it.albumNameTextField.copy(
                    text = name
                ),
                album = _uiState.value.album.copy(name = name)
            )
        }
        verifyAlbumNameInputError()
    }

    private fun verifyAlbumNameInputError(): Boolean {
        val error = _uiState.value.albumNameTextField.text.isEmpty()
        _uiState.update {
            it.copy(
                albumNameTextField = it.albumNameTextField.copy(
                    error = error,
                    errorMessage = R.string.error_empty_text_field
                )
            )
        }

        return error
    }

    private fun onAlbumImageUrlChange(url: String) {
        _uiState.update {
            it.copy(
                albumImageUrlTextField = it.albumImageUrlTextField.copy(text = url),
                album = _uiState.value.album.copy(albumImage = url)
            )
        }
    }

    private fun onNormalStickersFromChange(fromValue: String) {
        if (fromValue.toIntOrNull() != null || fromValue.isEmpty()) {
            _uiState.update {
                it.copy(
                    normalStickersFromTextField = it.normalStickersFromTextField.copy(
                        text = fromValue
                    )
                )
            }
            verifyNormalStickerInputError()
        }
    }

    private fun onNormalStickersToChange(toValue: String) {
        if (toValue.toIntOrNull() != null || toValue.isEmpty()) {
            _uiState.update {
                it.copy(
                    normalStickersToTextField = it.normalStickersToTextField.copy(text = toValue)
                )
            }
            verifyNormalStickerInputError()
        }
    }

    private fun verifyNormalStickerInputError(): Boolean {
        val (errorFrom, errorMessageFrom) = verifyInputNumber(
            _uiState.value.normalStickersFromTextField.text,
            _uiState.value.normalStickersToTextField.text,
            false
        )
        _uiState.update {
            it.copy(
                normalStickersFromTextField = it.normalStickersFromTextField.copy(
                    error = errorFrom,
                    errorMessage = errorMessageFrom
                )
            )
        }

        val (errorTo, errorMessageTo) = verifyInputNumber(
            _uiState.value.normalStickersToTextField.text,
            _uiState.value.normalStickersFromTextField.text,
            true
        )
        _uiState.update {
            it.copy(
                normalStickersToTextField = it.normalStickersToTextField.copy(
                    error = errorTo,
                    errorMessage = errorMessageTo
                )
            )
        }

        updateTotalStickers()

        return errorFrom || errorTo
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
        val from = letterFrom.lastOrNull()?.uppercase() ?: ""
        if (from.onlyLetters()) {
            _uiState.update {
                it.copy(
                    specialStickersLetterFromTextField = it.specialStickersLetterFromTextField.copy(
                        text = from
                    )
                )
            }
            verifySpecialStickerLetterInputError()
        }
    }

    private fun onSpecialStickersLetterToChange(letterTo: String) {
        val to = letterTo.lastOrNull()?.uppercase() ?: ""
        if (to.onlyLetters()) {
            _uiState.update {
                it.copy(
                    specialStickersLetterToTextField = it.specialStickersLetterToTextField.copy(text = to)
                )
            }
            verifySpecialStickerLetterInputError()
        }
    }

    private fun verifySpecialStickerLetterInputError(): Boolean {
        val (errorFrom, errorMessageFrom) = verifyInputLetter(
            _uiState.value.specialStickersLetterFromTextField.text,
            _uiState.value.specialStickersLetterToTextField.text,
            false
        )
        _uiState.update {
            it.copy(
                specialStickersLetterFromTextField = it.specialStickersLetterFromTextField.copy(
                    error = errorFrom,
                    errorMessage = errorMessageFrom
                )
            )
        }

        val (errorTo, errorMessageTo) = verifyInputLetter(
            _uiState.value.specialStickersLetterToTextField.text,
            _uiState.value.specialStickersLetterFromTextField.text,
            true
        )
        _uiState.update {
            it.copy(
                specialStickersLetterToTextField = it.specialStickersLetterToTextField.copy(
                    error = errorTo,
                    errorMessage = errorMessageTo
                )
            )
        }

        updateTotalStickers()

        return errorFrom || errorTo
    }

    private fun onSpecialStickersNumberFromChange(numberFrom: String) {
        if (numberFrom.toIntOrNull() != null || numberFrom.isEmpty()) {
            _uiState.update {
                it.copy(
                    specialStickersNumberFromTextField = it.specialStickersNumberFromTextField.copy(
                        text = numberFrom
                    )
                )
            }
            verifySpecialStickerNumberInputError()
        }
    }

    private fun onSpecialStickersNumberToChange(numberTo: String) {
        if (numberTo.toIntOrNull() != null || numberTo.isEmpty()) {
            _uiState.update {
                it.copy(
                    specialStickersNumberToTextField = it.specialStickersNumberToTextField.copy(text = numberTo)
                )
            }
            verifySpecialStickerNumberInputError()
        }
    }

    private fun verifySpecialStickerNumberInputError(): Boolean {
        val (errorFrom, errorMessageFrom) = verifyInputNumber(
            _uiState.value.specialStickersNumberFromTextField.text,
            _uiState.value.specialStickersNumberToTextField.text,
            false
        )
        _uiState.update {
            it.copy(
                specialStickersNumberFromTextField = it.specialStickersNumberFromTextField.copy(
                    error = errorFrom,
                    errorMessage = errorMessageFrom
                )
            )
        }

        val (errorTo, errorMessageTo) = verifyInputNumber(
            _uiState.value.specialStickersNumberToTextField.text,
            _uiState.value.specialStickersNumberFromTextField.text,
            true
        )
        _uiState.update {
            it.copy(
                specialStickersNumberToTextField = it.specialStickersNumberToTextField.copy(
                    error = errorTo,
                    errorMessage = errorMessageTo
                )
            )
        }

        updateTotalStickers()

        return errorFrom || errorTo
    }

    private fun onCreateClick(): Boolean {
        if (!hasError()) {
            val album = Album(
                name = _uiState.value.albumNameTextField.text,
                stickersList = createStickersList(),
                status = AlbumStatus.Completing,
                albumImage = _uiState.value.albumImageUrlTextField.text
            )

            viewModelScope.launch {
                withContext(IO) {
                    AlbumsRepository.addAlbum(album)
                }
            }

            return true
        }

        return false
    }

    private fun createStickersList(): StickersList {
        val stickers = ArrayList<Sticker>()
        stickers.addAll(createNormalStickersList())
        stickers.addAll(createSpecialStickersList())
        return StickersList(stickers)
    }

    private fun createNormalStickersList(): List<Sticker> {
        return (_uiState.value.normalStickersFromTextField.text.toInt().._uiState.value.normalStickersToTextField.text.toInt())
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
            (_uiState.value.specialStickersLetterFromTextField.text.uppercase()
                .single().._uiState.value.specialStickersLetterToTextField.text.uppercase()
                .single()).forEach { letter ->
                stickers.addAll(
                    (_uiState.value.specialStickersNumberFromTextField.text.toInt().._uiState.value.specialStickersNumberToTextField.text.toInt()).map { number ->
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

    private fun verifyInputNumber(
        mainValue: String,
        comparisonValue: String,
        greaterComparison: Boolean
    ): Pair<Boolean, Int> {
        return if (mainValue.isEmpty()) {
            Pair(true, R.string.error_empty_text_field)
        } else if (mainValue.toInt() == 0) {
            Pair(true, R.string.error_zero_text_field)
        } else if (greaterComparison && comparisonValue.toIntOrNull() != null && mainValue.toInt() <= comparisonValue.toInt()) {
            Pair(true, R.string.error_grater_text_field)
        } else if (!greaterComparison && comparisonValue.toIntOrNull() != null && mainValue.toInt() >= comparisonValue.toInt()) {
            Pair(true, R.string.error_smaller_text_field)
        } else {
            Pair(false, R.string.error_empty_text_field)
        }
    }

    private fun verifyInputLetter(
        mainValue: String,
        comparisonValue: String,
        greaterComparison: Boolean
    ): Pair<Boolean, Int> {
        return if (mainValue.isEmpty()) {
            Pair(true, R.string.error_empty_text_field)
        } else if (greaterComparison && comparisonValue.isNotEmpty() && mainValue <= comparisonValue) {
            Pair(true, R.string.error_grater_text_field)
        } else if (!greaterComparison && comparisonValue.isNotEmpty() && mainValue >= comparisonValue) {
            Pair(true, R.string.error_smaller_text_field)
        } else {
            Pair(false, R.string.error_empty_text_field)
        }
    }

    private fun hasError(): Boolean {
        var error = false
        if (verifyAlbumNameInputError()) error = true
        if (verifyNormalStickerInputError()) error = true
        if (_uiState.value.hasSpecialStickers) {
            if (verifySpecialStickerLetterInputError()) error = true
            if (verifySpecialStickerNumberInputError()) error = true
        }

        return error
    }

    private fun updateTotalStickers() {
        var total = 0
        _uiState.value.apply {
            val (errorFrom, _) = verifyInputNumber(
                normalStickersFromTextField.text,
                normalStickersToTextField.text,
                false
            )

            val (errorTo, _) = verifyInputNumber(
                normalStickersToTextField.text,
                normalStickersFromTextField.text,
                true
            )

            val (errorLetterFrom, _) = verifyInputLetter(
                specialStickersLetterFromTextField.text,
                specialStickersLetterToTextField.text,
                false
            )

            val (errorLetterTo, _) = verifyInputLetter(
                specialStickersLetterToTextField.text,
                specialStickersLetterFromTextField.text,
                true
            )

            val (errorNumberFrom, _) = verifyInputNumber(
                specialStickersNumberFromTextField.text,
                specialStickersNumberToTextField.text,
                false
            )

            val (errorNumberTo, _) = verifyInputNumber(
                specialStickersNumberToTextField.text,
                specialStickersNumberFromTextField.text,
                true
            )


            if (!errorFrom && !errorTo) {
                total += createNormalStickersList().size
            }

            if (!errorLetterFrom && !errorLetterTo && !errorNumberFrom && !errorNumberTo) {
                total += createSpecialStickersList().size
            }
        }

        _uiState.update {
            it.copy(
                totalStickers = total
            )
        }
    }

    private fun onReceivedAlbumName(albumName: String) {
        viewModelScope.launch {
            val album = withContext(IO) {
                return@withContext AlbumsRepository.getAlbumByName(albumName)
            } ?: return@launch

            _uiState.update {
                it.copy(
                    album = album
                )
            }

            onAlbumNameChange(album.name)
            onAlbumImageUrlChange(album.albumImage)
            onNormalStickersFromChange(album.getNormalStickers().first().identifier)
            onNormalStickersToChange(album.getNormalStickers().last().identifier)

            onHasSpecialStickersChange(album.getSpecialStickers().isNotEmpty())
            onSpecialStickerTypeChange(
                if (album.getSpecialStickers().first().identifier[0].toString()
                        .toIntOrNull() != null
                ) SpecialStickerType.LetterNumber else SpecialStickerType.NumberLetter
            )

            val lettersList =
                album.getSpecialStickers().map { it.identifier.replace(Regex("[0-9 ]"), "") }
                    .toSet()
            onSpecialStickersLetterFromChange(lettersList.first())
            onSpecialStickersLetterToChange(lettersList.last())
            val numbersList =
                album.getSpecialStickers().map { it.identifier.replace(Regex("[^0-9 ]"), "") }
                    .toSet()
            onSpecialStickersNumberFromChange(numbersList.first())
            onSpecialStickersNumberToChange(numbersList.last())


        }
    }
}