package com.example.mystickeralbum.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.mystickeralbum.R
import com.example.mystickeralbum.model.SpecialStickerType
import com.example.mystickeralbum.stateholders.AddAlbumUIState
import com.example.mystickeralbum.ui.TextField
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.example.mystickeralbum.viewmodels.AddAlbumViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAlbumActivity : ComponentActivity() {

    private val viewModel: AddAlbumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyStickerAlbumTheme {
                AddAlbumScreen(viewModel.uiState.collectAsState().value)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddAlbumScreen(state: AddAlbumUIState) {
        Scaffold(
            topBar = {
                AddAlbumTopBar()
            }
        ) {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        BasicAlbumInfo(state)
                        StickersInfo(state)
                    }

                    BottomButtons(state)
                }
            }
        }
    }

    @Composable
    fun BasicAlbumInfo(state: AddAlbumUIState) {
        Column {
            Text(
                text = stringResource(id = R.string.album_name_label),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp),
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
            TextField(
                text = state.albumNameTextField.text,
                onValueChange = {
                    state.albumNameTextField.onTextChange(it)
                },
                modifier = Modifier
                    .height(40.dp),
                textSize = 14.sp,
                paddingValues = PaddingValues(horizontal = 20.dp),
                error = state.albumNameTextField.error,
                errorMessage = stringResource(id = state.albumNameTextField.errorMessage)
            )
        }

        Column {
            Text(
                text = stringResource(id = R.string.image_url_label),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
            TextField(
                text = state.albumImageUrlTextField.text,
                onValueChange = {
                    state.albumImageUrlTextField.onTextChange(it)
                },
                modifier = Modifier
                    .height(40.dp),
                textSize = 14.sp,
                paddingValues = PaddingValues(horizontal = 20.dp)
            )
        }

        Column {
            Text(
                text = stringResource(id = R.string.preview_label),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
            PreviewAlbum(
                name = state.albumNameTextField.text,
                imageUrl = state.albumImageUrlTextField.text,
                totalStickers = state.totalStickers
            )
        }
    }

    @Composable
    fun PreviewAlbum(name: String, imageUrl: String, totalStickers: Int) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 10.dp),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.secondary
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5F),
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.5F)),
                        contentScale = ContentScale.Crop
                    )

                    if (name.isNotEmpty()) {
                        Text(
                            text = name,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 10.dp,
                                        bottomEnd = 0.dp,
                                        bottomStart = 0.dp
                                    )
                                )
                                .align(Alignment.BottomStart)
                                .padding(horizontal = 10.dp),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = stringResource(id = R.string.total_stickers_label) + " $totalStickers",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

    @Composable
    fun StickersInfo(state: AddAlbumUIState) {
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
    fun NormalStickerInfo(state: AddAlbumUIState) {
        Text(
            text = stringResource(id = R.string.normal_stickers_label),
            fontSize = 16.sp,
            modifier = Modifier
                .padding(horizontal = 20.dp),
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
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
                    .fillMaxHeight()
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
                    .fillMaxHeight()
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
                    .fillMaxHeight()
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
    fun SpecialStickerInfo(state: AddAlbumUIState) {
        Row(
            modifier = Modifier
                .height(30.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    text = stringResource(id = R.string.special_stickers_label),
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            Switch(
                checked = state.hasSpecialStickers,
                onCheckedChange = {
                    state.onHasSpecialStickersChange(it)
                },
                modifier = Modifier
            )
        }


        if (state.hasSpecialStickers) {
            Row(
                modifier = Modifier
                    .height(30.dp)
                    .padding(horizontal = 20.dp)
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
                        fontWeight = FontWeight.Normal,
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
    fun LetterInputSpecialSticker(state: AddAlbumUIState) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    text = stringResource(id = R.string.letter_label),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(end = 6.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
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
                    .fillMaxHeight()
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
                    .fillMaxHeight()
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
                    .fillMaxHeight()
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
    fun NumberInputSpecialSticker(state: AddAlbumUIState) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    text = stringResource(id = R.string.number_label),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(end = 6.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
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
                    .fillMaxHeight()
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
                    .fillMaxHeight()
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
                    .fillMaxHeight()
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
    fun BottomButtons(state: AddAlbumUIState) {
        Row(
            modifier = Modifier
                .height(60.dp)
                .padding(horizontal = 20.dp, 10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Button(
                onClick = { finish() },
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth(),
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
                onClick = {
                    if (state.onCreateClick()) finish()
                },
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.create_button),
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddAlbumTopBar() {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.add_album_title),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    }

    @Preview(showSystemUi = true)
    @Composable
    fun AddAlbumScreenPreview() {
        MyStickerAlbumTheme {
            AddAlbumScreen(AddAlbumUIState(hasSpecialStickers = true))
        }
    }
}