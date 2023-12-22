package com.devgc.mystickeralbum.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devgc.mystickeralbum.AlbumsRepository
import com.devgc.mystickeralbum.MyStickerAlbumApplication
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.model.CheckboxValues
import com.devgc.mystickeralbum.model.CompoundStickerType
import com.devgc.mystickeralbum.model.DialogValues
import com.devgc.mystickeralbum.model.EditStickerMode
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.StickersList
import com.devgc.mystickeralbum.model.TextFieldValues
import com.devgc.mystickeralbum.model.ToggleGroupValues
import com.devgc.mystickeralbum.navigation.MainNavComponent
import com.devgc.mystickeralbum.navigation.MainNavComponent.Companion.getSingleTopWithPopUpTo
import com.devgc.mystickeralbum.navigation.MainNavComponent.Companion.navController
import com.devgc.mystickeralbum.navigation.screens.UpdateAlbumScreen
import com.devgc.mystickeralbum.ui.stateholders.CreateEditAlbumUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateEditAlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<CreateEditAlbumUIState> =
        MutableStateFlow(CreateEditAlbumUIState())
    val uiState get() = _uiState.asStateFlow()

    private var oldAlbum = _uiState.value.album

    init {
        _uiState.update {
            it.copy(
                albumNameTextField = TextFieldValues(onTextChange = ::onAlbumNameChange),
                albumImageUrlTextField = TextFieldValues(onTextChange = ::onAlbumImageUrlChange),
                numberStickerFromTextField = TextFieldValues(onTextChange = ::onNumberStickerFromChange),
                numberStickerToTextField = TextFieldValues(onTextChange = ::onNumberStickerToChange),
                numberCheckbox = CheckboxValues(onCheckedChange = ::onNumberCheckboxChange),
                textStickerFromTextField = TextFieldValues(onTextChange = ::onTextStickerFromChange),
                textStickerToTextField = TextFieldValues(onTextChange = ::onTextStickerToChange),
                textCheckbox = CheckboxValues(onCheckedChange = ::onTextCheckboxChange),
                compoundTypeToggle = ToggleGroupValues(
                    options = CompoundStickerType.getOptionsString(MyStickerAlbumApplication.getInstance()),
                    onOptionClick = ::onCompoundTypeChange
                ),
                onCreateEditClick = ::onCreateEditClick,
                onCancelClick = ::onCancelClick,
                compoundStickerDialog = DialogValues(changeDialogState = ::changeCompoundStickerTypeDialogState),
                onAddStickersClick = ::onAddStickersClick,
                onRemoveStickersClick = ::onRemoveStickersClick,
                editModeToggle = ToggleGroupValues(
                    options = EditStickerMode.getOptionsString(MyStickerAlbumApplication.getInstance()),
                    onOptionClick = ::onEditModeChange
                )
            )
        }

        CoroutineScope(IO).launch {
            savedStateHandle
                .getStateFlow<String?>(MainNavComponent.albumNameArgument, null)
                .filterNotNull()
                .collect { albumName ->
                    onReceivedAlbumName(albumName)
                }
        }
    }

    private fun onAlbumNameChange(name: String) {
        viewModelScope.launch {
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
    }

    private suspend fun verifyAlbumNameInputError(): Boolean {
        val (error, errorMessage) = verifyAlbumName(_uiState.value.albumNameTextField.text)
        _uiState.update {
            it.copy(
                albumNameTextField = it.albumNameTextField.copy(
                    error = error,
                    errorMessage = errorMessage
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

    private fun onNumberStickerFromChange(text: String, verifyError: Boolean = true) {
        if (text.toIntOrNull() != null || text.isEmpty()) {
            _uiState.update {
                it.copy(
                    numberStickerFromTextField = it.numberStickerFromTextField.copy(text = text)
                )
            }
            if (verifyError) verifyNumberStickerInputError()
            updateEditStickersPreview()
        }
    }

    private fun onNumberStickerToChange(text: String, verifyError: Boolean = true) {
        if (text.toIntOrNull() != null || text.isEmpty()) {
            _uiState.update {
                it.copy(
                    numberStickerToTextField = it.numberStickerToTextField.copy(text = text)
                )
            }
            if (verifyError) verifyNumberStickerInputError()
            updateEditStickersPreview()
        }
    }

    private fun onNumberCheckboxChange(checked: Boolean) {
        _uiState.update {
            it.copy(
                numberCheckbox = it.numberCheckbox.copy(checked = checked),
                numberStickerToTextField = it.numberStickerToTextField.copy(text = "")
            )
        }
        updateEditStickersPreview()
    }

    private fun onTextStickerFromChange(text: String, verifyError: Boolean = true) {
        _uiState.update {
            it.copy(
                textStickerFromTextField = it.textStickerFromTextField.copy(text = text)
            )
        }
        if (verifyError) verifyTextStickerInputError()
        updateEditStickersPreview()
    }

    private fun onTextStickerToChange(text: String, verifyError: Boolean = true) {
        _uiState.update {
            it.copy(
                textStickerToTextField = it.textStickerToTextField.copy(text = text)
            )
        }
        if (verifyError) verifyTextStickerInputError()
        updateEditStickersPreview()
    }

    private fun onTextCheckboxChange(checked: Boolean) {
        _uiState.update {
            it.copy(
                textCheckbox = it.textCheckbox.copy(checked = checked),
                textStickerToTextField = it.textStickerToTextField.copy(text = "")
            )
        }
        updateEditStickersPreview()
    }

    private fun onCompoundTypeChange(index: Int) {
        _uiState.update {
            it.copy(
                compoundTypeToggle = it.compoundTypeToggle.copy(selectedIndex = index)
            )
        }
        updateEditStickersPreview()
    }

    private fun verifyNumberStickerInputError(): Boolean {
        val (errorFrom, errorMessageFrom) = verifyInputNumber(
            _uiState.value.numberStickerFromTextField.text,
            _uiState.value.numberStickerToTextField.text,
            false,
            _uiState.value.numberCheckbox.checked
        )
        _uiState.update {
            it.copy(
                numberStickerFromTextField = it.numberStickerFromTextField.copy(
                    error = errorFrom,
                    errorMessage = errorMessageFrom
                )
            )
        }

        val (errorTo, errorMessageTo) = verifyInputNumber(
            _uiState.value.numberStickerToTextField.text,
            _uiState.value.numberStickerFromTextField.text,
            true,
            _uiState.value.numberCheckbox.checked
        )
        _uiState.update {
            it.copy(
                numberStickerToTextField = it.numberStickerToTextField.copy(
                    error = errorTo,
                    errorMessage = errorMessageTo
                )
            )
        }

        return errorFrom || errorTo
    }

    private fun verifyTextStickerInputError(): Boolean {
        val (errorFrom, errorMessageFrom) = verifyInputText(
            _uiState.value.textStickerFromTextField.text,
            _uiState.value.textStickerToTextField.text,
            false,
            _uiState.value.textCheckbox.checked
        )
        _uiState.update {
            it.copy(
                textStickerFromTextField = it.textStickerFromTextField.copy(
                    error = errorFrom,
                    errorMessage = errorMessageFrom
                )
            )
        }

        val (errorTo, errorMessageTo) = verifyInputText(
            _uiState.value.textStickerToTextField.text,
            _uiState.value.textStickerFromTextField.text,
            true,
            _uiState.value.textCheckbox.checked
        )
        _uiState.update {
            it.copy(
                textStickerToTextField = it.textStickerToTextField.copy(
                    error = errorTo,
                    errorMessage = errorMessageTo
                )
            )
        }

        return errorFrom || errorTo
    }

    private fun onCreateEditClick() {
        viewModelScope.launch {
            if (!hasError()) {
                val album = if (_uiState.value.isCreateAlbum) {
                    _uiState.value.album
                } else {
                    val newStickersList = _uiState.value.album.stickersList.stickers
                    val oldStickerList = oldAlbum.stickersList.stickers

                    val resultSticker = ArrayList<Sticker>()
                    newStickersList.forEach { newSticker ->
                        val oldSticker =
                            oldStickerList.find { it.identifier == newSticker.identifier }
                        if (oldSticker == null) {
                            newSticker.let {
                                resultSticker.add(it)
                            }
                        } else {
                            resultSticker.add(oldSticker)
                        }
                    }

                    _uiState.value.album.copy(stickersList = StickersList(resultSticker))
                }

                viewModelScope.launch {
                    withContext(IO) {
                        AlbumsRepository.updateAlbum(album, oldAlbum)
                    }

                    if (_uiState.value.isCreateAlbum) {
                        navController.popBackStack()
                    } else {
                        navController.apply {
                            UpdateAlbumScreen.apply {
                                navigateToItself(
                                    albumName = album.name,
                                    navOptions = getSingleTopWithPopUpTo(routeScreen, true)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onCancelClick() {
        navController.popBackStack()
    }

    private suspend fun verifyAlbumName(albumName: String): Pair<Boolean, Int> {
        return if (albumName.isEmpty()) {
            Pair(true, R.string.error_empty_text_field)
        } else {
            val existingAlbum = withContext(IO) { AlbumsRepository.getAlbumByName(albumName) }
            if (existingAlbum != null && _uiState.value.isCreateAlbum) {
                Pair(true, R.string.error_album_name_exists)
            } else {
                Pair(false, R.string.error_empty_text_field)
            }
        }
    }

    private fun verifyInputNumber(
        mainValue: String,
        comparisonValue: String,
        greaterComparison: Boolean,
        rangeValue: Boolean
    ): Pair<Boolean, Int> {
        return if (rangeValue) {
            if (mainValue.isEmpty()) {
                Pair(true, R.string.error_empty_text_field)
            } else if (greaterComparison && comparisonValue.toIntOrNull() != null && mainValue.toInt() <= comparisonValue.toInt()) {
                Pair(true, R.string.error_grater_text_field)
            } else if (!greaterComparison && comparisonValue.toIntOrNull() != null && mainValue.toInt() >= comparisonValue.toInt()) {
                Pair(true, R.string.error_smaller_text_field)
            } else {
                Pair(false, R.string.error_empty_text_field)
            }
        } else {
            Pair(false, R.string.error_empty_text_field)
        }
    }

    private fun verifyInputText(
        mainValue: String,
        comparisonValue: String,
        greaterComparison: Boolean,
        rangeValue: Boolean
    ): Pair<Boolean, Int> {
        return if (rangeValue) {
            if (mainValue.isEmpty()) {
                Pair(true, R.string.error_empty_text_field)
            } else if (mainValue.length > 1) {
                Pair(true, R.string.error_char_text_field)
            } else if (greaterComparison && comparisonValue.isNotEmpty() && mainValue <= comparisonValue) {
                Pair(true, R.string.error_grater_text_field)
            } else if (!greaterComparison && comparisonValue.isNotEmpty() && mainValue >= comparisonValue) {
                Pair(true, R.string.error_smaller_text_field)
            } else {
                Pair(false, R.string.error_empty_text_field)
            }
        } else {
            Pair(false, R.string.error_empty_text_field)
        }
    }

    private fun updateEditStickersPreview() {
        val previewList = ArrayList<Sticker>()
        val numberInputError =
            _uiState.value.numberStickerFromTextField.error || _uiState.value.numberStickerToTextField.error
        val textInputError =
            _uiState.value.textStickerFromTextField.error || _uiState.value.textStickerToTextField.error
        if (!numberInputError && !textInputError) {
            val numberFrom = _uiState.value.numberStickerFromTextField.text
            val textFrom = _uiState.value.textStickerFromTextField.text

            if (numberFrom.isEmpty() && textFrom.isNotEmpty()) {
                previewList.addAll(
                    createTextStickers()
                )
            } else if (textFrom.isEmpty() && numberFrom.isNotEmpty()) {
                previewList.addAll(
                    createNumberStickers()
                )
            } else if (numberFrom.isNotEmpty() && textFrom.isNotEmpty()) {
                previewList.addAll(
                    createCompoundStickers()
                )
            }
        }

        _uiState.update {
            it.copy(
                toBeAddStickersList = previewList
            )
        }
    }

    private fun createNumberStickers(): List<Sticker> {
        val list = ArrayList<Sticker>()

        val fromValue = _uiState.value.numberStickerFromTextField.text.toIntOrNull()
        val toValue = _uiState.value.numberStickerToTextField.text.toIntOrNull()

        if (fromValue != null) {
            if (toValue == null) {
                list.add(
                    Sticker(
                        identifier = fromValue.toString(),
                        found = false,
                        repeated = 0
                    )
                )
            } else {
                list.addAll(
                    (fromValue..toValue).map {
                        Sticker(
                            identifier = it.toString(),
                            found = false,
                            repeated = 0
                        )
                    }
                )
            }
        }

        return list
    }

    private fun createTextStickers(): List<Sticker> {
        val list = ArrayList<Sticker>()

        val fromValue = _uiState.value.textStickerFromTextField.text
        val toValue = _uiState.value.textStickerToTextField.text

        if (fromValue.isNotEmpty()) {
            if (toValue.isEmpty()) {
                list.add(
                    Sticker(
                        identifier = fromValue,
                        found = false,
                        repeated = 0
                    )
                )
            } else {
                if (fromValue.length == 1 && toValue.length == 1) {
                    list.addAll(
                        (fromValue.single()..toValue.single()).map {
                            Sticker(
                                identifier = it.toString(),
                                found = false,
                                repeated = 0
                            )
                        }
                    )
                }
            }
        }

        return list
    }

    private fun createCompoundStickers(): List<Sticker> {
        val list = ArrayList<Sticker>()

        val numberFrom = _uiState.value.numberStickerFromTextField.text
        val numberTo = _uiState.value.numberStickerToTextField.text
        val textFrom = _uiState.value.textStickerFromTextField.text
        val textTo = _uiState.value.textStickerToTextField.text

        if (numberTo.isEmpty() && textTo.isEmpty()) {
            list.add(
                Sticker(
                    identifier = createCompoundIdentifier(numberFrom, textFrom),
                    found = false,
                    repeated = 0
                )
            )
        } else if (numberTo.isNotEmpty() && textTo.isEmpty()) {
            val numberFromInt = numberFrom.toInt()
            val numberToInt = numberTo.toInt()
            list.addAll(
                (numberFromInt..numberToInt).map {
                    Sticker(
                        identifier = createCompoundIdentifier(it.toString(), textFrom),
                        found = false,
                        repeated = 0
                    )
                }
            )
        } else if (numberTo.isEmpty() && textTo.isNotEmpty()) {
            if (textFrom.length == 1 && textTo.length == 1) {
                list.addAll(
                    (textFrom.single()..textTo.single()).map {
                        Sticker(
                            identifier = createCompoundIdentifier(numberFrom, it.toString()),
                            found = false,
                            repeated = 0
                        )
                    }
                )
            }
        } else if (numberTo.isNotEmpty() && textTo.isNotEmpty()) {
            (textFrom.single()..textTo.single()).forEach { text ->
                list.addAll(
                    (numberFrom.toInt()..numberTo.toInt()).map { number ->
                        Sticker(
                            identifier = createCompoundIdentifier(
                                number.toString(),
                                text.toString()
                            ),
                            found = false,
                            repeated = 0
                        )
                    }
                )
            }
        }

        return list
    }

    private fun createCompoundIdentifier(numberValue: String, textValue: String): String {
        val compoundType =
            CompoundStickerType.values()[_uiState.value.compoundTypeToggle.selectedIndex]
        return if (compoundType == CompoundStickerType.LetterNumber) {
            "$textValue$numberValue"
        } else {
            "$numberValue$textValue"
        }
    }

    private fun onAddStickersClick() {
        if (!hasEditStickersError()) {
            _uiState.update {
                val currentAlbum = it.album
                val newStickers = it.toBeAddStickersList
                val updatedStickers = ArrayList(it.album.stickersList.stickers)
                newStickers.forEach { newSticker ->
                    val contains = updatedStickers.find { updatedSticker ->
                        newSticker.identifier == updatedSticker.identifier
                    } != null
                    if (!contains) {
                        updatedStickers.add(newSticker)
                    }
                }
                it.copy(
                    album = currentAlbum.copy(stickersList = StickersList(updatedStickers)),
                )
            }
            onNumberStickerFromChange("", false)
            onNumberStickerToChange("", false)
            onTextStickerFromChange("", false)
            onTextStickerToChange("", false)
            onNumberCheckboxChange(false)
            onTextCheckboxChange(false)
        }
    }

    private fun onRemoveStickersClick() {
        if (!hasEditStickersError()) {
            _uiState.update {
                val currentAlbum = it.album
                val removeStickers = it.toBeAddStickersList
                val updatedStickers = ArrayList(currentAlbum.stickersList.stickers)
                removeStickers.forEach { removeSticker ->
                    val index = updatedStickers.indexOfFirst { updatedSticker ->
                        removeSticker.identifier == updatedSticker.identifier
                    }
                    if (index != -1) {
                        updatedStickers.removeAt(index)
                    }
                }
                it.copy(
                    album = currentAlbum.copy(stickersList = StickersList(updatedStickers)),
                )
            }
            onNumberStickerFromChange("", false)
            onNumberStickerToChange("", false)
            onTextStickerFromChange("", false)
            onTextStickerToChange("", false)
            onNumberCheckboxChange(false)
            onTextCheckboxChange(false)
        }
    }

    private suspend fun hasError(): Boolean {
        return verifyAlbumNameInputError() || hasEditStickersError()
    }

    private fun hasEditStickersError(): Boolean {
        return verifyNumberStickerInputError() || verifyTextStickerInputError()
    }

    private fun onReceivedAlbumName(albumName: String) {
        viewModelScope.launch {
            val album = withContext(IO) {
                return@withContext AlbumsRepository.getAlbumByName(albumName)
            } ?: run {
                _uiState.update {
                    it.copy(
                        isCreateAlbum = true
                    )
                }
                return@launch
            }

            oldAlbum = album

            _uiState.update {
                it.copy(
                    album = album,
                    isCreateAlbum = false
                )
            }

            onAlbumNameChange(album.name)
            onAlbumImageUrlChange(album.albumImage)
        }
    }

    private fun changeCompoundStickerTypeDialogState() {
        _uiState.update {
            it.copy(
                compoundStickerDialog = it.compoundStickerDialog.copy(showDialog = !_uiState.value.compoundStickerDialog.showDialog)
            )
        }
    }

    private fun onEditModeChange(index: Int) {
        _uiState.update {
            it.copy(
                editModeToggle = it.editModeToggle.copy(selectedIndex = index),
                currentEditMode = EditStickerMode.getByIndex(index)
            )
        }
    }
}