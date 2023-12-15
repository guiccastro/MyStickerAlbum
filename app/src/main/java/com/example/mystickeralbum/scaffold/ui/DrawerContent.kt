package com.example.mystickeralbum.scaffold.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.mystickeralbum.scaffold.models.DrawerMenuItem
import com.example.mystickeralbum.scaffold.stateholders.DrawerMenuUIState
import com.example.mystickeralbum.scaffold.maincomponents.MainDrawerMenuComponent
import com.example.mystickeralbum.scaffold.maincomponents.MainDrawerMenuComponent.onClickDrawerMenuItem
import com.example.mystickeralbum.scaffold.stateholders.TopAppBarUIState
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun DrawerContent(state: DrawerMenuUIState) {
    ModalDrawerSheet(
        modifier = Modifier
            .width(250.dp),
        drawerContainerColor = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_app),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
            )

            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.SemiBold
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = Color.White
            )

            MainDrawerMenuComponent.drawerItems.forEach { drawerItem ->
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(id = drawerItem.title),
                            fontSize = 14.sp,
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = drawerItem.icon),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    },
                    modifier = Modifier
                        .height(50.dp),
                    selected = drawerItem == state.itemSelected,
                    onClick = {
                        onClickDrawerMenuItem(drawerItem)
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.White.copy(alpha = 0.2F),
                        unselectedContainerColor = Color.Transparent
                    )
                )
            }

        }
    }
}

@Preview
@Composable
fun DrawerContentPreview() {
    MyStickerAlbumTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .width(300.dp)
                ) {
                    DrawerContent(
                        state = DrawerMenuUIState(itemSelected = DrawerMenuItem.SettingsDrawerMenuItem)
                    )
                }
            },
        ) {
            Scaffold(
                topBar = {
                    TopBar(
                        state = TopAppBarUIState(title = R.string.album_list_title),
                    )
                }
            ) {
                Surface(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }
    }
}