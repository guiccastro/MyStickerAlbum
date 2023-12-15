package com.example.mystickeralbum.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.mystickeralbum.model.MainTopAppBarComponent
import com.example.mystickeralbum.ui.stateholders.TopAppBarUIState
import com.example.mystickeralbum.model.MainDrawerMenuComponent
import com.example.mystickeralbum.model.MainDrawerMenuComponent.changeDrawerState
import com.example.mystickeralbum.model.MainFABComponent
import com.example.mystickeralbum.ui.stateholders.DrawerMenuUIState
import com.example.mystickeralbum.ui.stateholders.FabUIState
import kotlinx.coroutines.launch

@Composable
fun MainScaffold(
    topAppBarState: TopAppBarUIState = MainTopAppBarComponent.topAppBarState(),
    fabState: FabUIState = MainFABComponent.fabState(),
    drawerMenuState: DrawerMenuUIState = MainDrawerMenuComponent.drawerMenuState(),
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val state = rememberDrawerState(drawerMenuState.drawerValue, confirmStateChange = {
        changeDrawerState(it)
        true
    })

    state.apply {
        scope.launch {
            if (drawerMenuState.drawerValue == DrawerValue.Closed) {
                close()
            } else {
                open()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = state,
        drawerContent = {
            DrawerContent(drawerMenuState)
        },
    ) {
        Scaffold(
            floatingActionButton = { FabApp(fabState) },
            topBar = {
                TopBar(
                    state = topAppBarState,
                    onMenuClick = {
                        scope.launch {
                            changeDrawerState()
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