package com.example.mystickeralbum

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.mystickeralbum.model.TopBarState
import com.example.mystickeralbum.ui.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    floatingActionButton: @Composable () -> Unit = {},
    topBarState: TopBarState,
    content: @Composable () -> Unit
) {
    val state = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = state,
        drawerContent = {
            DrawerContent()
        },
    ) {
        Scaffold(
            floatingActionButton = { floatingActionButton() },
            topBar = {
                TopBar(
                    topBarState = topBarState,
                    onMenuClick = {
                        scope.launch {
                            if (state.isClosed) state.open() else state.close()
                        }
                    }
                )
            }
        ) {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                content()
            }
        }
    }

}