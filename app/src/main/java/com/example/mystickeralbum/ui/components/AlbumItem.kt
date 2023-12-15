package com.example.mystickeralbum.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mystickeralbum.R
import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.model.StickersList
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun AlbumCard(
    album: Album,
    onClick: ((Album) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .clickable(
                enabled = onClick != null
            ) {
                if (onClick != null) {
                    onClick(album)
                }
            },
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
                    model = album.albumImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.5F)),
                    contentScale = ContentScale.Crop
                )

                if (album.name.isNotEmpty()) {
                    Text(
                        text = album.name,
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

            content()
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
                .align(Alignment.CenterVertically),
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
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(album.getProgress())
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7F))
                )

                Text(
                    text = album.getFormattedProgress(),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
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
                        .align(Alignment.CenterVertically),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = album.getTotalStickers().toString(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 4.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row {
                Text(
                    text = stringResource(id = R.string.album_item_found),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = album.getFound().size.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 4.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row {
                Text(
                    text = stringResource(id = R.string.album_item_missing),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = album.getMissing().size.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 4.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row {
                Text(
                    text = stringResource(id = R.string.album_item_repeated),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = album.getRepeated().size.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 4.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun AlbumStickerInfo(album: Album) {
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

@Preview
@Composable
fun AlbumCardPreview() {
    MyStickerAlbumTheme {
        val album = Album(
            name = "Album Name",
            stickersList = StickersList(
                listOf(
                    Sticker(
                        "1",
                        true,
                        2
                    ),
                    Sticker(
                        "2",
                        false,
                        0
                    )
                )
            ),
            status = AlbumStatus.Completing,
            albumImage = ""
        )
        AlbumCard(
            album = album
        ) {
            AlbumStickerInfo(album = album)
        }
    }
}