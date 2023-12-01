package com.example.mystickeralbum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
                Column {
                    Text(
                        text = stringResource(id = R.string.album_name_label),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(horizontal = 20.dp),
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                    var nameText by rememberSaveable { mutableStateOf("") }
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

                    Text(
                        text = stringResource(id = R.string.image_url_label),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(horizontal = 20.dp),
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                    var imageUrlText by rememberSaveable { mutableStateOf("") }
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
        }
    }

    @Composable
    fun PreviewAlbum(name: String, imageUrl: String) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 20.dp),
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
            AddAlbumScreen()
        }
    }
}