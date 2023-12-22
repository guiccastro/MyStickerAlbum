package com.devgc.mystickeralbum.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.extensions.toGrid
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.ButtonItem
import com.devgc.mystickeralbum.model.CheckboxValues
import com.devgc.mystickeralbum.model.CompoundStickerType
import com.devgc.mystickeralbum.model.EditStickerMode
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.TextFieldValues
import com.devgc.mystickeralbum.model.ToggleGroupValues
import com.devgc.mystickeralbum.ui.components.AlbumCard
import com.devgc.mystickeralbum.ui.components.SimpleDialog
import com.devgc.mystickeralbum.ui.components.TextField
import com.devgc.mystickeralbum.ui.components.TitleSection
import com.devgc.mystickeralbum.ui.components.ToggleGroup
import com.devgc.mystickeralbum.ui.stateholders.CreateEditAlbumUIState
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.devgc.mystickeralbum.ui.viewmodels.CreateEditAlbumViewModel

@Composable
fun CreateEditAlbumUIScreen(viewModel: CreateEditAlbumViewModel) {
    val state = viewModel.uiState.collectAsState().value
    CreateEditAlbumUIScreen(state)

    if (state.compoundStickerDialog.showDialog) {
        CompoundStickerTypeDialog(state.compoundStickerDialog.changeDialogState)
    }
}

@Composable
fun CreateEditAlbumUIScreen(state: CreateEditAlbumUIState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            BasicAlbumInfo(state)
            EditStickers(state)
        }

        BottomButtons(state)
    }
}

@Composable
fun BasicAlbumInfo(state: CreateEditAlbumUIState) {
    Column {
        Text(
            text = stringResource(id = R.string.album_name_label).uppercase(),
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (0.1).sp
        )
        TextField(
            text = state.albumNameTextField.text,
            onValueChange = {
                state.albumNameTextField.onTextChange(it)
            },
            modifier = Modifier
                .height(40.dp),
            textSize = 14.sp,
            error = state.albumNameTextField.error,
            errorMessage = stringResource(id = state.albumNameTextField.errorMessage)
        )
    }

    Column {
        Text(
            text = stringResource(id = R.string.image_url_label).uppercase(),
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (0.1).sp
        )
        TextField(
            text = state.albumImageUrlTextField.text,
            onValueChange = {
                state.albumImageUrlTextField.onTextChange(it)
            },
            modifier = Modifier
                .height(40.dp),
            textSize = 14.sp
        )
    }

    Column {
        Text(
            text = stringResource(id = R.string.preview_label).uppercase(),
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (0.1).sp
        )
        PreviewAlbum(album = state.album)
    }
}

@Composable
fun PreviewAlbum(album: Album) {
    AlbumCard(
        album = album
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.total_stickers_label).uppercase(),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = album.stickersList.stickers.size.toString(),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun EditStickers(state: CreateEditAlbumUIState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleSection(
            title = stringResource(
                id = EditStickerMode.getByIndex(state.editModeToggle.selectedIndex).getTitle()
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        EditModeSelect(editModeToggle = state.editModeToggle)

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1F)
            ) {
                StickerInput(
                    title = stringResource(id = R.string.number_label),
                    textFieldFromValues = state.numberStickerFromTextField,
                    textFieldToValues = state.numberStickerToTextField,
                    keyboardTypeFrom = KeyboardType.Number,
                    keyboardTypeTo = KeyboardType.Number,
                    checkboxValues = state.numberCheckbox
                )
            }

            Box(
                modifier = Modifier
                    .weight(1F)
            ) {
                StickerInput(
                    title = stringResource(id = R.string.text_label),
                    textFieldFromValues = state.textStickerFromTextField,
                    textFieldToValues = state.textStickerToTextField,
                    checkboxValues = state.textCheckbox
                )
            }
        }

        CompoundStickerTypeSelect(state = state)

        StickersToBeEditedPreview(
            editStickerMode = state.currentEditMode,
            stickersList = state.toBeAddStickersList,
            onAddStickerClick = state.onAddStickersClick,
            onRemoveStickerClick = state.onRemoveStickersClick
        )

        StickersPreview(
            title = stringResource(id = R.string.current_stickers_label),
            stickersList = state.album.stickersList.stickers,
            emptyMessage = stringResource(id = R.string.current_stickers_empty),
            columnsGrid = 10
        )
    }
}

@Composable
fun EditModeSelect(editModeToggle: ToggleGroupValues) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.edit_mode_title),
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )
        ToggleGroup(toggleGroupValues = editModeToggle)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickerInput(
    title: String,
    textFieldFromValues: TextFieldValues,
    textFieldToValues: TextFieldValues,
    keyboardTypeFrom: KeyboardType = KeyboardType.Text,
    keyboardTypeTo: KeyboardType = KeyboardType.Text,
    checkboxValues: CheckboxValues
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )

        Row {
            TextField(
                text = textFieldFromValues.text,
                onValueChange = {
                    textFieldFromValues.onTextChange(it)
                },
                modifier = Modifier
                    .height(32.dp)
                    .width(50.dp),
                textSize = 14.sp,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardTypeFrom),
                error = textFieldFromValues.error,
                errorMessage = stringResource(id = textFieldFromValues.errorMessage),
                errorModifier = Modifier
                    .width(50.dp)
            )

            if (checkboxValues.checked) {
                Box(
                    modifier = Modifier
                        .height(32.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_long_arrow_right),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                TextField(
                    text = textFieldToValues.text,
                    onValueChange = {
                        textFieldToValues.onTextChange(it)
                    },
                    modifier = Modifier
                        .height(32.dp)
                        .width(50.dp),
                    textSize = 14.sp,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardTypeTo),
                    error = textFieldToValues.error,
                    errorMessage = stringResource(id = textFieldToValues.errorMessage),
                    errorModifier = Modifier
                        .width(50.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .height(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    checkboxValues.onCheckedChange(!checkboxValues.checked)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    checked = checkboxValues.checked,
                    onCheckedChange = checkboxValues.onCheckedChange,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                )
            }

            Text(
                text = stringResource(id = R.string.range_checkbox_desc),
                fontSize = 10.sp,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CompoundStickerTypeSelect(state: CreateEditAlbumUIState) {
    if (state.numberStickerFromTextField.text.isNotEmpty() && state.textStickerFromTextField.text.isNotEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.compound_sticker_type_label).uppercase(),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_about_app),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .clickable {
                            state.compoundStickerDialog.changeDialogState()
                        }
                )
            }

            ToggleGroup(toggleGroupValues = state.compoundTypeToggle)
        }
    }
}

@Composable
fun CompoundStickerTypeDialog(onClose: () -> Unit) {
    SimpleDialog(
        title = stringResource(id = R.string.compound_sticker_type_title),
        description = stringResource(id = R.string.compound_sticker_type_desc),
        negativeButton = ButtonItem(text = null, onClick = onClose)
    )
}

@Composable
fun StickersToBeEditedPreview(
    editStickerMode: EditStickerMode,
    stickersList: List<Sticker>,
    onAddStickerClick: () -> Unit,
    onRemoveStickerClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StickersPreview(
            title = if (editStickerMode == EditStickerMode.AddStickers) stringResource(id = R.string.stickers_to_be_added) else stringResource(
                id = R.string.stickers_to_be_removed
            ),
            stickersList = stickersList,
            emptyMessage = if (editStickerMode == EditStickerMode.AddStickers) stringResource(id = R.string.stickers_to_be_add_empty) else stringResource(
                id = R.string.stickers_to_be_removed_empty
            ),
            columnsGrid = 10
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (editStickerMode == EditStickerMode.AddStickers) {
                Text(
                    text = stringResource(id = R.string.add_stickers),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .shadow(4.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            onAddStickerClick()
                        }
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(10.dp))
                        .padding(vertical = 2.dp, horizontal = 6.dp)
                )
            } else {
                Text(
                    text = stringResource(id = R.string.remove_stickers),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .shadow(4.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            onRemoveStickerClick()
                        }
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(vertical = 2.dp, horizontal = 6.dp)
                )
            }
        }
    }
}

@Composable
fun StickersPreview(
    title: String,
    stickersList: List<Sticker>,
    emptyMessage: String,
    columnsGrid: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleSection(
            title = title.uppercase(),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (stickersList.isEmpty()) {
                Text(
                    text = emptyMessage,
                    fontSize = 10.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium
                )
            } else {
                val grid = stickersList.toGrid(columnsGrid)
                grid.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach {
                            Text(
                                text = it.identifier,
                                fontSize = 10.sp,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .weight(1F)
                                    .border((0.1).dp, MaterialTheme.colorScheme.secondary)
                            )
                        }

                        repeat(columnsGrid - row.size) {
                            Spacer(modifier = Modifier.weight(1F))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomButtons(state: CreateEditAlbumUIState) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { state.onCancelClick() },
            modifier = Modifier
                .weight(1F)
                .fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            )
        ) {
            Text(
                text = stringResource(id = R.string.cancel_button),
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        }

        Button(
            onClick = { state.onCreateEditClick() },
            modifier = Modifier
                .weight(1F)
                .fillMaxSize(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(id = if (state.isCreateAlbum) R.string.create_button else R.string.edit_button),
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AddAlbumScreenPreview() {
    MyStickerAlbumTheme {
        CreateEditAlbumUIScreen(
            CreateEditAlbumUIState(
                numberStickerFromTextField = TextFieldValues(text = "1"),
                textStickerFromTextField = TextFieldValues(text = "A"),
                editModeToggle = ToggleGroupValues(EditStickerMode.getOptionsString(LocalContext.current)),
                compoundTypeToggle = ToggleGroupValues(
                    CompoundStickerType.getOptionsString(
                        LocalContext.current
                    )
                )
            )
        )
    }
}