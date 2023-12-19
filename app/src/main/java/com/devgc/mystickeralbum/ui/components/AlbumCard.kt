package com.devgc.mystickeralbum.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.AlbumStatus
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.StickersList
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun AlbumCard(
    album: Album,
    onClick: ((Album) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val hasImage = album.albumImage.isNotEmpty()
    val isTablet = booleanResource(id = R.bool.isTablet)
    val baseHeight = if (isTablet) 300.dp else 200.dp
    val height = if (hasImage) {
        baseHeight
    } else {
        baseHeight / 1.5F
    }
    val albumNameHeight = if (isTablet) 26.sp else 20.sp
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clickable(
                enabled = onClick != null
            ) {
                if (onClick != null) {
                    onClick(album)
                }
            },
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (hasImage) {
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
                            fontSize = albumNameHeight,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.secondary,
                                    RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 10.dp,
                                        bottomEnd = 0.dp,
                                        bottomStart = 0.dp
                                    )
                                )
                                .align(Alignment.BottomStart)
                                .padding(horizontal = 10.dp),
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = album.name,
                        fontSize = albumNameHeight,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondary)
                            .align(Alignment.Center)
                            .padding(horizontal = 10.dp),
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }

            content()
        }
    }
}

@Composable
fun AlbumProgress(album: Album, isTablet: Boolean) {
    val progressHeight = if (isTablet) 34.dp else 20.dp
    val fontSize = if (isTablet) 20.sp else 14.sp
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(progressHeight),
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
                    .background(MaterialTheme.colorScheme.secondary)
            )

            Text(
                text = album.getFormattedProgress(),
                fontSize = fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center),
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun AlbumStickersInfo(album: Album, isTablet: Boolean) {
    val spacerHeight = if (isTablet) 100.dp else 60.dp
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StickerInfoComponent(
            icon = R.drawable.ic_total,
            value = album.getTotalStickers().toString(),
            isTablet = isTablet
        )

        Spacer(
            modifier = Modifier
                .height(spacerHeight)
                .width(1.dp)
                .padding(vertical = 4.dp)
                .background(Color.White),
        )

        StickerInfoComponent(
            icon = R.drawable.ic_found,
            value = album.getFound().size.toString(),
            isTablet = isTablet
        )

        Spacer(
            modifier = Modifier
                .height(spacerHeight)
                .width(1.dp)
                .padding(vertical = 4.dp)
                .background(Color.White),
        )

        StickerInfoComponent(
            icon = R.drawable.ic_missing,
            value = album.getMissing().size.toString(),
            isTablet = isTablet
        )

        Spacer(
            modifier = Modifier
                .height(spacerHeight)
                .width(1.dp)
                .padding(vertical = 4.dp)
                .background(Color.White),
        )

        StickerInfoComponent(
            icon = R.drawable.ic_repeated,
            value = album.getRepeated().size.toString(),
            isTablet = isTablet
        )
    }

}

@Composable
fun RowScope.StickerInfoComponent(@DrawableRes icon: Int, value: String, isTablet: Boolean) {
    val iconHeight = if (isTablet) 50.dp else 30.dp
    val fontSize = if (isTablet) 20.sp else 14.sp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .weight(1F, false)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .height(iconHeight)
                .aspectRatio(1F),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )

        Text(
            text = value,
            fontSize = fontSize,
            modifier = Modifier
                .padding(horizontal = 2.dp),
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@Composable
fun AlbumStickerInfo(album: Album) {
    val isTablet = booleanResource(id = R.bool.isTablet)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        AlbumProgress(album, isTablet)
        AlbumStickersInfo(album, isTablet)
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