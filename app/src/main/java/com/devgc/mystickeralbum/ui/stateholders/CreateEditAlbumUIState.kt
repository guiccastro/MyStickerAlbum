package com.devgc.mystickeralbum.ui.stateholders

import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.AlbumStatus
import com.devgc.mystickeralbum.model.CheckboxValues
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.StickersList
import com.devgc.mystickeralbum.model.TextFieldValues
import com.devgc.mystickeralbum.model.ToggleGroupValues

data class CreateEditAlbumUIState(
    val albumNameTextField: TextFieldValues = TextFieldValues(),
    val albumImageUrlTextField: TextFieldValues = TextFieldValues(),
    val numberStickerFromTextField: TextFieldValues = TextFieldValues(),
    val numberStickerToTextField: TextFieldValues = TextFieldValues(),
    val numberCheckbox: CheckboxValues = CheckboxValues(),
    val textStickerFromTextField: TextFieldValues = TextFieldValues(),
    val textStickerToTextField: TextFieldValues = TextFieldValues(),
    val textCheckbox: CheckboxValues = CheckboxValues(),
    val compoundTypeToggle: ToggleGroupValues = ToggleGroupValues(),
    val onCreateEditClick: () -> Unit = { },
    val onCancelClick: () -> Unit = { },
    val album: Album = Album("", StickersList(emptyList()), AlbumStatus.Completing, ""),
    val isCreateAlbum: Boolean = true,
    val showCompoundStickerTypeDialog: Boolean = false,
    val changeCompoundStickerTypeDialogState: () -> Unit = {},
    val toBeAddStickersList: List<Sticker> = emptyList(),
    val onAddStickersClick: () -> Unit = {},
    val onRemoveStickersClick: () -> Unit = {}
)
