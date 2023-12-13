package com.example.mystickeralbum

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mystickeralbum.model.TopBarState
import com.example.mystickeralbum.ui.TopBar
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent() {
    ModalDrawerSheet(
        modifier = Modifier
            .width(300.dp),
        drawerContainerColor = MaterialTheme.colorScheme.primary
    ) {
        Column {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                    DrawerContent()
                }
            },
        ) {
            Scaffold(
                topBar = {
                    TopBar(
                        topBarState = TopBarState(title = R.string.album_list_title),
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