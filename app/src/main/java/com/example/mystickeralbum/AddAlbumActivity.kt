package com.example.mystickeralbum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.mystickeralbum.model.SpecialStickerType
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme

class AddAlbumActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyStickerAlbumTheme {
                AddAlbumScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddAlbumScreen() {
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
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    BasicAlbumInfo()
                    StickersInfo()
                }
            }
        }
    }

    @Composable
    fun BasicAlbumInfo() {
        var nameText by rememberSaveable { mutableStateOf("") }
        var imageUrlText by rememberSaveable { mutableStateOf("") }

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
                text = nameText,
                onValueChange = {
                    nameText = it
                },
                modifier = Modifier
                    .height(40.dp),
                textSize = 14.sp,
                paddingValues = PaddingValues(horizontal = 20.dp)
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
                text = imageUrlText,
                onValueChange = {
                    imageUrlText = it
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
            PreviewAlbum(name = nameText, imageUrl = imageUrlText)
        }
    }

    @Composable
    fun PreviewAlbum(name: String, imageUrl: String) {
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
                                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7F),
                                    RoundedCornerShape(10.dp)
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
                        text = stringResource(id = R.string.total_stickers_label),
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
    fun StickersInfo() {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            NormalStickerInfo()
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SpecialStickerInfo()
        }
    }

    @Composable
    fun NormalStickerInfo() {
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
                .height(30.dp)
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
            var fromText by rememberSaveable { mutableStateOf("") }
            TextField(
                text = fromText,
                onValueChange = {
                    fromText = it
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp),
                textSize = 14.sp,
                paddingValues = PaddingValues(horizontal = 10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
            var toText by rememberSaveable { mutableStateOf("") }
            TextField(
                text = toText,
                onValueChange = {
                    toText = it
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp),
                textSize = 14.sp,
                paddingValues = PaddingValues(horizontal = 10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }

    @Composable
    fun SpecialStickerInfo() {
        var specialStickersChecked by remember { mutableStateOf(false) }
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
                checked = specialStickersChecked,
                onCheckedChange = {
                    specialStickersChecked = it
                },
                modifier = Modifier
            )
        }


        if (specialStickersChecked) {
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

                var specialStickerType by rememberSaveable { mutableStateOf(SpecialStickerType.LetterNumber) }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .zIndex(if (specialStickerType == SpecialStickerType.LetterNumber) 1F else 0F)
                            .then(
                                if (specialStickerType == SpecialStickerType.LetterNumber) Modifier.background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(4.dp, 0.dp, 0.dp, 4.dp)
                                ) else Modifier.background(
                                    Color.Gray,
                                    RoundedCornerShape(4.dp, 0.dp, 0.dp, 4.dp)
                                )
                            )
                            .then(
                                if (specialStickerType == SpecialStickerType.LetterNumber) Modifier.border(
                                    1.dp,
                                    Color.Black,
                                    RoundedCornerShape(4.dp, 0.dp, 0.dp, 4.dp)
                                ) else Modifier.border(
                                    1.dp,
                                    Color.DarkGray,
                                    RoundedCornerShape(4.dp, 0.dp, 0.dp, 4.dp)
                                )
                            )
                            .clickable { specialStickerType = SpecialStickerType.LetterNumber }
                    ) {
                        Text(
                            text = stringResource(id = R.string.letter_number),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 4.dp),
                            overflow = TextOverflow.Ellipsis,
                            color = if (specialStickerType == SpecialStickerType.LetterNumber) Color.White else Color.Black,
                            fontWeight = if (specialStickerType == SpecialStickerType.LetterNumber) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .offset(x = (-1).dp)
                            .zIndex(if (specialStickerType == SpecialStickerType.NumberLetter) 1F else 0F)
                            .then(
                                if (specialStickerType == SpecialStickerType.NumberLetter) Modifier.background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(0.dp, 4.dp, 4.dp, 0.dp)
                                ) else Modifier.background(
                                    Color.Gray,
                                    RoundedCornerShape(0.dp, 4.dp, 4.dp, 0.dp)
                                )
                            )
                            .then(
                                if (specialStickerType == SpecialStickerType.NumberLetter) Modifier.border(
                                    1.dp,
                                    Color.Black,
                                    RoundedCornerShape(0.dp, 4.dp, 4.dp, 0.dp)
                                ) else Modifier.border(
                                    1.dp,
                                    Color.DarkGray,
                                    RoundedCornerShape(0.dp, 4.dp, 4.dp, 0.dp)
                                )
                            )
                            .clickable { specialStickerType = SpecialStickerType.NumberLetter }
                    ) {
                        Text(
                            text = stringResource(id = R.string.number_letter),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 4.dp),
                            overflow = TextOverflow.Ellipsis,
                            color = if (specialStickerType == SpecialStickerType.NumberLetter) Color.White else Color.Black,
                            fontWeight = if (specialStickerType == SpecialStickerType.NumberLetter) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }

            LetterInputSpecialSticker()
            NumberInputSpecialSticker()
        }
    }

    @Composable
    fun LetterInputSpecialSticker() {
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
            var fromText by rememberSaveable { mutableStateOf("") }
            TextField(
                text = fromText,
                onValueChange = {
                    fromText = it
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp),
                textSize = 14.sp,
                paddingValues = PaddingValues(horizontal = 10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
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
            var toText by rememberSaveable { mutableStateOf("") }
            TextField(
                text = toText,
                onValueChange = {
                    toText = it
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp),
                textSize = 14.sp,
                paddingValues = PaddingValues(horizontal = 10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }
    }

    @Composable
    fun NumberInputSpecialSticker() {
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
            var fromText by rememberSaveable { mutableStateOf("") }
            TextField(
                text = fromText,
                onValueChange = {
                    fromText = it
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp),
                textSize = 14.sp,
                paddingValues = PaddingValues(horizontal = 10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
            var toText by rememberSaveable { mutableStateOf("") }
            TextField(
                text = toText,
                onValueChange = {
                    toText = it
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp),
                textSize = 14.sp,
                paddingValues = PaddingValues(horizontal = 10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
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
            AddAlbumScreen()
        }
    }
}