package com.devgc.mystickeralbum.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.AlbumStatus
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.StickersList
import com.devgc.mystickeralbum.ui.components.AlbumCard
import com.devgc.mystickeralbum.ui.components.AlbumStickerInfo
import com.devgc.mystickeralbum.ui.components.BaseDialog
import com.devgc.mystickeralbum.ui.stateholders.AlbumsListUIState
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.devgc.mystickeralbum.ui.viewmodels.AlbumsListViewModel

@Composable
fun AlbumsListUIScreen(viewModel: AlbumsListViewModel) {
    val state = viewModel.uiState.collectAsState().value
    AlbumsListUIScreen(state)
}

@Composable
fun AlbumsListUIScreen(state: AlbumsListUIState) {
    LaunchedEffect(Unit) {
        state.updateAlbumsList()
    }
    if (state.albumsList.isEmpty()) {
        Text(
            text = stringResource(id = R.string.album_list_empty),
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(state.albumsList) {
                AlbumItem(
                    album = it,
                    state = state
                )
            }
        }
    }

    if (state.showIconsLegendDialog) {
        IconsLegendDialog(state)
    }
}

@Composable
fun AlbumItem(album: Album, state: AlbumsListUIState) {
    AlbumCard(
        album = album,
        onClick = state.onAlbumClick
    ) {
        AlbumStickerInfo(album)
    }
}

@Composable
fun IconsLegendDialog(state: AlbumsListUIState) {
    BaseDialog(
        onDismissRequest = state.changeIconsLegendDialogState,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.icons_legend_dialog_title).uppercase(),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        IconLegend(
            icon = R.drawable.ic_total,
            legend = R.string.album_item_total
        )

        Divider(
            modifier = Modifier
                .width(180.dp),
            thickness = (0.5).dp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        IconLegend(
            icon = R.drawable.ic_found,
            legend = R.string.album_item_found
        )

        Divider(
            modifier = Modifier
                .width(180.dp),
            thickness = (0.5).dp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        IconLegend(
            icon = R.drawable.ic_missing,
            legend = R.string.album_item_missing
        )

        Divider(
            modifier = Modifier
                .width(180.dp),
            thickness = (0.5).dp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        IconLegend(
            icon = R.drawable.ic_repeated,
            legend = R.string.album_item_repeated
        )
    }
}

@Composable
fun IconLegend(
    @DrawableRes icon: Int,
    @StringRes legend: Int
) {
    Row(
        modifier = Modifier
            .width(180.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .aspectRatio(1F),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
        )

        Text(
            text = stringResource(id = legend),
            fontSize = 18.sp,
            modifier = Modifier
                .weight(1F)
                .padding(start = 10.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun AlbumsScreenPreview() {
    MyStickerAlbumTheme {
        AlbumsListUIScreen(
            state = AlbumsListUIState(
                albumsList = listOf(
                    Album(
                        "Album 1",
                        StickersList(
                            listOf(
                                Sticker("1", false, 0),
                                Sticker("2", true, 1),
                                Sticker("3", true, 2)
                            )
                        ),
                        AlbumStatus.Completing,
                        "https://i0.wp.com/maquinadoesporte.com.br/wp-content/uploads/2023/10/foto-maquina-do-esporte-1200-675-3-8.png?fit=616%2C308&ssl=1"
                    ),
                    Album(
                        "Album 2",
                        StickersList(
                            listOf(
                                Sticker("1", false, 0),
                                Sticker("2", false, 0),
                                Sticker("3", false, 0)
                            )
                        ),
                        AlbumStatus.Completing,
                        "https://ultraverso.com.br/wp-content/uploads/2023/07/2-1.jpg"
                    )
                )
            )
        )
    }
}

@Preview
@Composable
fun IconsLegendDialogPreview() {
    MyStickerAlbumTheme {
        IconsLegendDialog(
            state = AlbumsListUIState()
        )
    }
}