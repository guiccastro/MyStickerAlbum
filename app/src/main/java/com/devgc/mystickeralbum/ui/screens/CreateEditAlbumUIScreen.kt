package com.devgc.mystickeralbum.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.SpecialStickerType
import com.devgc.mystickeralbum.ui.components.AlbumCard
import com.devgc.mystickeralbum.ui.components.TextField
import com.devgc.mystickeralbum.ui.stateholders.CreateEditAlbumUIState
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.devgc.mystickeralbum.ui.viewmodels.CreateEditAlbumViewModel

@Composable
fun CreateEditAlbumUIScreen(viewModel: CreateEditAlbumViewModel) {
    val state = viewModel.uiState.collectAsState().value
    CreateEditAlbumUIScreen(state)
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
            StickersInfo(state)
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
        PreviewAlbum(
            album = state.album,
            totalStickers = state.totalStickers
        )
    }
}

@Composable
fun PreviewAlbum(album: Album, totalStickers: Int) {
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
                text = totalStickers.toString(),
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
fun StickersInfo(state: CreateEditAlbumUIState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        NormalStickerInfo(state)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        SpecialStickerInfo(state)
    }
}

@Composable
fun NormalStickerInfo(state: CreateEditAlbumUIState) {
    Text(
        text = stringResource(id = R.string.normal_stickers_label).uppercase(),
        fontSize = 16.sp,
        overflow = TextOverflow.Ellipsis,
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (0.1).sp
    )

    Row {
        Box(
            modifier = Modifier
                .height(32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.from_label),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.Center),
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
        TextField(
            text = state.normalStickersFromTextField.text,
            onValueChange = {
                state.normalStickersFromTextField.onTextChange(it)
            },
            modifier = Modifier
                .height(32.dp)
                .width(50.dp),
            textSize = 14.sp,
            paddingValues = PaddingValues(horizontal = 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            error = state.normalStickersFromTextField.error,
            errorMessage = stringResource(id = state.normalStickersFromTextField.errorMessage),
            errorModifier = Modifier
                .width(50.dp)
        )

        Box(
            modifier = Modifier
                .height(32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.to_label),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.Center),
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
        TextField(
            text = state.normalStickersToTextField.text,
            onValueChange = {
                state.normalStickersToTextField.onTextChange(it)
            },
            modifier = Modifier
                .height(32.dp)
                .width(50.dp),
            textSize = 14.sp,
            paddingValues = PaddingValues(horizontal = 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            error = state.normalStickersToTextField.error,
            errorMessage = stringResource(id = state.normalStickersToTextField.errorMessage),
            errorModifier = Modifier
                .width(50.dp)
        )
    }
}

@Composable
fun SpecialStickerInfo(state: CreateEditAlbumUIState) {
    Row(
        modifier = Modifier
            .height(30.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Text(
                text = stringResource(id = R.string.special_stickers_label).uppercase(),
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Center),
                letterSpacing = (0.1).sp
            )
        }

        Switch(
            checked = state.hasSpecialStickers,
            onCheckedChange = {
                state.onHasSpecialStickersChange(it)
            },
            modifier = Modifier
                .scale(0.9F)
        )
    }


    if (state.hasSpecialStickers) {
        Row(
            modifier = Modifier
                .height(30.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    text = stringResource(id = R.string.type_label),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .zIndex(if (state.specialStickerType == SpecialStickerType.LetterNumber) 1F else 0F)
                        .then(
                            if (state.specialStickerType == SpecialStickerType.LetterNumber) Modifier.background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(4.dp, 0.dp, 0.dp, 4.dp)
                            ) else Modifier.background(
                                Color.Gray,
                                RoundedCornerShape(4.dp, 0.dp, 0.dp, 4.dp)
                            )
                        )
                        .then(
                            if (state.specialStickerType == SpecialStickerType.LetterNumber) Modifier.border(
                                1.dp,
                                Color.Black,
                                RoundedCornerShape(4.dp, 0.dp, 0.dp, 4.dp)
                            ) else Modifier.border(
                                1.dp,
                                Color.DarkGray,
                                RoundedCornerShape(4.dp, 0.dp, 0.dp, 4.dp)
                            )
                        )
                        .clickable { state.onSpecialStickerTypeChange(SpecialStickerType.LetterNumber) }
                ) {
                    Text(
                        text = stringResource(id = R.string.letter_number),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 4.dp),
                        overflow = TextOverflow.Ellipsis,
                        color = if (state.specialStickerType == SpecialStickerType.LetterNumber) Color.White else Color.Black,
                        fontWeight = if (state.specialStickerType == SpecialStickerType.LetterNumber) FontWeight.SemiBold else FontWeight.Normal
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .offset(x = (-1).dp)
                        .zIndex(if (state.specialStickerType == SpecialStickerType.NumberLetter) 1F else 0F)
                        .then(
                            if (state.specialStickerType == SpecialStickerType.NumberLetter) Modifier.background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(0.dp, 4.dp, 4.dp, 0.dp)
                            ) else Modifier.background(
                                Color.Gray,
                                RoundedCornerShape(0.dp, 4.dp, 4.dp, 0.dp)
                            )
                        )
                        .then(
                            if (state.specialStickerType == SpecialStickerType.NumberLetter) Modifier.border(
                                1.dp,
                                Color.Black,
                                RoundedCornerShape(0.dp, 4.dp, 4.dp, 0.dp)
                            ) else Modifier.border(
                                1.dp,
                                Color.DarkGray,
                                RoundedCornerShape(0.dp, 4.dp, 4.dp, 0.dp)
                            )
                        )
                        .clickable { state.onSpecialStickerTypeChange(SpecialStickerType.NumberLetter) }
                ) {
                    Text(
                        text = stringResource(id = R.string.number_letter),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 4.dp),
                        overflow = TextOverflow.Ellipsis,
                        color = if (state.specialStickerType == SpecialStickerType.NumberLetter) Color.White else Color.Black,
                        fontWeight = if (state.specialStickerType == SpecialStickerType.NumberLetter) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        LetterInputSpecialSticker(state)
        NumberInputSpecialSticker(state)
    }
}

@Composable
fun LetterInputSpecialSticker(state: CreateEditAlbumUIState) {
    Row {
        Box(
            modifier = Modifier
                .height(32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.letter_label),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(end = 6.dp)
            )
        }

        Box(
            modifier = Modifier
                .height(32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.from_label),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.Center),
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
        TextField(
            text = state.specialStickersLetterFromTextField.text,
            onValueChange = {
                state.specialStickersLetterFromTextField.onTextChange(it)
            },
            modifier = Modifier
                .height(32.dp)
                .width(50.dp),
            textSize = 14.sp,
            paddingValues = PaddingValues(horizontal = 10.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Characters
            ),
            error = state.specialStickersLetterFromTextField.error,
            errorMessage = stringResource(id = state.specialStickersLetterFromTextField.errorMessage),
            errorModifier = Modifier
                .width(50.dp)
        )

        Box(
            modifier = Modifier
                .height(32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.to_label),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.Center),
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
        TextField(
            text = state.specialStickersLetterToTextField.text,
            onValueChange = {
                state.specialStickersLetterToTextField.onTextChange(it)
            },
            modifier = Modifier
                .height(32.dp)
                .width(50.dp),
            textSize = 14.sp,
            paddingValues = PaddingValues(horizontal = 10.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Characters
            ),
            error = state.specialStickersLetterToTextField.error,
            errorMessage = stringResource(id = state.specialStickersLetterToTextField.errorMessage),
            errorModifier = Modifier
                .width(50.dp)
        )
    }
}

@Composable
fun NumberInputSpecialSticker(state: CreateEditAlbumUIState) {
    Row {
        Box(
            modifier = Modifier
                .height(32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.number_label),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(end = 6.dp)
            )
        }

        Box(
            modifier = Modifier
                .height(32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.from_label),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.Center),
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
        TextField(
            text = state.specialStickersNumberFromTextField.text,
            onValueChange = {
                state.specialStickersNumberFromTextField.onTextChange(it)
            },
            modifier = Modifier
                .height(32.dp)
                .width(50.dp),
            textSize = 14.sp,
            paddingValues = PaddingValues(horizontal = 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            error = state.specialStickersNumberFromTextField.error,
            errorMessage = stringResource(id = state.specialStickersNumberFromTextField.errorMessage),
            errorModifier = Modifier
                .width(50.dp)
        )

        Box(
            modifier = Modifier
                .height(32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.to_label),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.Center),
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
        TextField(
            text = state.specialStickersNumberToTextField.text,
            onValueChange = {
                state.specialStickersNumberToTextField.onTextChange(it)
            },
            modifier = Modifier
                .height(32.dp)
                .width(50.dp),
            textSize = 14.sp,
            paddingValues = PaddingValues(horizontal = 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            error = state.specialStickersNumberToTextField.error,
            errorMessage = stringResource(id = state.specialStickersNumberToTextField.errorMessage),
            errorModifier = Modifier
                .width(50.dp)
        )
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

@Preview(showSystemUi = true)
@Composable
fun AddAlbumScreenPreview() {
    MyStickerAlbumTheme {
        CreateEditAlbumUIScreen(CreateEditAlbumUIState(hasSpecialStickers = true))
    }
}