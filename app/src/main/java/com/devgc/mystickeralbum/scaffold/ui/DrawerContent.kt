package com.devgc.mystickeralbum.scaffold.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DrawerValue
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devgc.mystickeralbum.BuildConfig
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.scaffold.maincomponents.MainDrawerMenuComponent.onClickDrawerMenuItem
import com.devgc.mystickeralbum.scaffold.models.DrawerMenuItem
import com.devgc.mystickeralbum.scaffold.stateholders.DrawerMenuUIState
import com.devgc.mystickeralbum.scaffold.stateholders.TopAppBarUIState
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun DrawerContent(state: DrawerMenuUIState) {
    ModalDrawerSheet(
        modifier = Modifier
            .width(250.dp),
        drawerContainerColor = Color(0xFF071827)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_app_transparent),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 10.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFF1D7BE0).copy(alpha = 0.35F),
                                Color(0xFF1D7BE0).copy(alpha = 0.25F),
                                Color(0xFF071827).copy(alpha = 0.15F),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
            )

            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                thickness = 2.dp,
                color = Color.White.copy(alpha = 0.18F)
            )

            DrawerMenuItem.values().forEach { drawerItem ->
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
                        selectedContainerColor = Color.White.copy(alpha = 0.14F),
                        unselectedContainerColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1F))

            Text(
                text = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                color = Color.White.copy(alpha = 0.72F),
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 10.sp
            )
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
            }
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
                    color = Color(0xFF071420)
                ) {
                }
            }
        }
    }
}
