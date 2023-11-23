package com.example.mystickeralbum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStickerAlbumTheme {
                AlbumsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen() {
    Scaffold(
        floatingActionButton = {
            AddAlbumFab()
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AlbumsList()
        }
    }
}

@Composable
fun AlbumsList() {
    val albumsList = listOf(
        Album(
            "Album 1",
            listOf(Sticker("1", false, 0), Sticker("2", true, 1), Sticker("3", true, 2)),
            AlbumStatus.Completing,
            "https://i0.wp.com/maquinadoesporte.com.br/wp-content/uploads/2023/10/foto-maquina-do-esporte-1200-675-3-8.png?fit=616%2C308&ssl=1"
        ),
        Album(
            "Album 2",
            listOf(Sticker("1", false, 0), Sticker("2", false, 0), Sticker("3", false, 0)),
            AlbumStatus.Completing,
            "https://ultraverso.com.br/wp-content/uploads/2023/07/2-1.jpg"
        )
    )
    LazyColumn() {
        items(albumsList) {
            AlbumItem(it)
        }
    }
}

@Composable
fun AlbumItem(album: Album) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        shape = RoundedCornerShape(10.dp),
        color = Color.LightGray
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
                    model = album.albumImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = album.name,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.7F), RoundedCornerShape(10.dp))
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 10.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                AlbumProgress(album)
                AlbumStickersInfo(album)
            }
        }
    }
}

@Composable
fun AlbumProgress(album: Album) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(id = R.string.album_item_progress),
            fontSize = 14.sp,
            modifier = Modifier
                .align(CenterVertically),
            overflow = TextOverflow.Ellipsis
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color.White
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(album.getProgress()),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = album.getFormattedProgress(),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Center),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun AlbumStickersInfo(album: Album) {

    Column {
        Text(
            text = stringResource(id = R.string.album_item_stickers),
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(
                    text = stringResource(id = R.string.album_item_total),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(CenterVertically),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = album.getTotalStickers().toString(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(CenterVertically)
                        .padding(horizontal = 4.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row {
                Text(
                    text = stringResource(id = R.string.album_item_found),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(CenterVertically),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = album.getFound().size.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(CenterVertically)
                        .padding(horizontal = 4.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row {
                Text(
                    text = stringResource(id = R.string.album_item_missing),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(CenterVertically),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = album.getMissing().size.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(CenterVertically)
                        .padding(horizontal = 4.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row {
                Text(
                    text = stringResource(id = R.string.album_item_repeated),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(CenterVertically),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = album.getRepeated().size.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(CenterVertically)
                        .padding(horizontal = 4.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }

}

@Composable
fun AddAlbumFab() {
    SmallFloatingActionButton(
        onClick = { /*TODO*/ }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = null
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun AlbumsScreenPreview() {
    MyStickerAlbumTheme {
        AlbumsScreen()
    }
}