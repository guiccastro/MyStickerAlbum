package com.example.mystickeralbum.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystickeralbum.R
import com.example.mystickeralbum.model.TopBarItem
import com.example.mystickeralbum.model.TopBarState
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    topBarState: TopBarState,
    onMenuClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = topBarState.title),
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
        ),
        navigationIcon = {
            if (topBarState.onReturn != null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_long_arrow_right),
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(180F)
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable { topBarState.onReturn.invoke() },
                    colorFilter = ColorFilter.tint(Color.White)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable { onMenuClick() },
                    colorFilter = ColorFilter.tint(Color.White),
                    alignment = Alignment.Center
                )
            }
        },
        actions = {
            topBarState.itemsList.forEach {
                Image(
                    painter = painterResource(id = it.image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(horizontal = 6.dp)
                        .clip(CircleShape)
                        .clickable { it.onClick() },
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun TopBarPreview() {
    MyStickerAlbumTheme {
        Scaffold(
            topBar = {
                TopBar(
                    topBarState = TopBarState(
                        title = R.string.album_list_title,
                        itemsList = listOf(
                            TopBarItem(R.drawable.ic_delete) {},
                            TopBarItem(R.drawable.ic_edit) {}
                        )
                    )
                )
            }
        ) {
            Box(modifier = Modifier.padding(it))
        }
    }
}