package com.devgc.mystickeralbum.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.AlbumStatus
import com.devgc.mystickeralbum.model.ImageCropperHelper
import com.devgc.mystickeralbum.model.ImageCropperHelper.getImageState
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.StickersList
import com.devgc.mystickeralbum.navigation.MainNavComponent
import com.devgc.mystickeralbum.navigation.NavigationParameters
import com.devgc.mystickeralbum.navigation.screens.CropImageScreen
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun AlbumCard(
    album: Album,
    onClick: ((Album) -> Unit)? = null,
    canEditImage: Boolean = false,
    content: @Composable () -> Unit
) {
    val imageState = ImageCropperHelper.imageStateHolder.collectAsState().value
    val context = LocalContext.current
    val hasImage = album.albumImage.isNotEmpty()
    val isTablet = booleanResource(id = R.bool.isTablet)
    val height = if (isTablet) 300.dp else 210.dp
    val albumNameHeight = if (isTablet) 26.sp else 20.sp
    val shape = RoundedCornerShape(18.dp)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .shadow(10.dp, shape)
            .clip(shape)
            .background(Color(0xFF0B2D4A), shape)
            .border(1.dp, Color.White.copy(alpha = 0.14F), shape)
            .clickable(
                enabled = onClick != null
            ) {
                if (onClick != null) {
                    onClick(album)
                }
            },
        color = Color.Transparent,
        shape = shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF0F4169),
                            Color(0xFF071827)
                        )
                    )
                )
        ) {
            var aspectRatio = 1F
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(if (hasImage) 0.48F else 0.34F)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF35A9E8),
                                Color(0xFF16598F)
                            )
                        )
                    )
                    .graphicsLayer {
                        aspectRatio = size.width / size.height
                    },
            ) {

                if (hasImage) {
                    imageState.getImageState(context, album.albumImage)?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.5F))
                        )
                    }
                }

                if (canEditImage) {
                    Button(
                        onClick = {
                            MainNavComponent.navController.apply {
                                CropImageScreen.apply {
                                    navigateToItself(
                                        parameters = NavigationParameters(
                                            albumName = album.name,
                                            aspectRatio = aspectRatio
                                        )
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.18F),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }

                if (album.name.isNotEmpty()) {
                    Text(
                        text = album.name,
                        fontSize = albumNameHeight,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
                            .background(
                                Color(0xFF071827).copy(alpha = 0.72F),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 3.dp),
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                } else if (!hasImage) {
                    Text(
                        text = "MSA",
                        fontSize = albumNameHeight,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontWeight = FontWeight.Bold,
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
        color = Color.White.copy(alpha = 0.16F)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(album.getProgress())
                    .background(Color(0xFF37D4B5))
            )

            Text(
                text = album.getFormattedProgress(),
                fontSize = fontSize,
                textAlign = TextAlign.Center,
                color = Color.White,
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
            icon = R.drawable.ic_stat_total,
            value = album.getTotalStickers().toString(),
            color = Color.White,
            isTablet = isTablet
        )

        Spacer(
            modifier = Modifier
                .height(spacerHeight)
                .width(1.dp)
                .padding(vertical = 4.dp)
                .background(Color.White.copy(alpha = 0.16F)),
        )

        StickerInfoComponent(
            icon = R.drawable.ic_stat_owned,
            value = album.getFound().size.toString(),
            color = Color(0xFF6EE7B7),
            isTablet = isTablet
        )

        Spacer(
            modifier = Modifier
                .height(spacerHeight)
                .width(1.dp)
                .padding(vertical = 4.dp)
                .background(Color.White.copy(alpha = 0.16F)),
        )

        StickerInfoComponent(
            icon = R.drawable.ic_stat_missing,
            value = album.getMissing().size.toString(),
            color = Color(0xFFFF7A88),
            isTablet = isTablet
        )

        Spacer(
            modifier = Modifier
                .height(spacerHeight)
                .width(1.dp)
                .padding(vertical = 4.dp)
                .background(Color.White.copy(alpha = 0.16F)),
        )

        StickerInfoComponent(
            icon = R.drawable.ic_stat_repeated,
            value = album.getRepeated().size.toString(),
            color = Color(0xFFFFD166),
            isTablet = isTablet
        )
    }

}

@Composable
fun RowScope.StickerInfoComponent(
    @DrawableRes icon: Int,
    value: String,
    color: Color,
    isTablet: Boolean
) {
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
            colorFilter = ColorFilter.tint(color)
        )

        Text(
            text = value,
            fontSize = fontSize,
            color = color,
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
