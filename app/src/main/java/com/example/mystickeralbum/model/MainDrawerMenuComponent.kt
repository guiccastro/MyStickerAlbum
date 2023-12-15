package com.example.mystickeralbum.model

import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.mystickeralbum.navigation.MainNavComponent
import com.example.mystickeralbum.navigation.interfaces.Screen
import com.example.mystickeralbum.ui.stateholders.DrawerMenuUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object MainDrawerMenuComponent {

    private var drawerMenuState: MutableStateFlow<DrawerMenuUIState> =
        MutableStateFlow(DrawerMenuUIState())

    val drawerItems: List<DrawerMenuItem> = DrawerMenuItem.values().toList()

    @Composable
    fun drawerMenuState(): DrawerMenuUIState {
        return drawerMenuState.asStateFlow().collectAsState().value
    }

    fun updateDrawerMenuState(newState: DrawerMenuUIState) {
        drawerMenuState.value = newState
    }

    fun onClickDrawerMenuItem(drawerItem: DrawerMenuItem) {
        MainNavComponent.navController.apply {
            drawerItem.screen.apply {
                navigateToItself(navOptions = MainNavComponent.getSingleTopWithPopUpTo(routeScreen))
            }
        }
        changeDrawerState(DrawerValue.Closed)
    }

    fun findItemByScreen(screen: Screen?): DrawerMenuItem? {
        if (screen == null) return null
        return drawerItems.find { it.screen == screen }
    }

    fun changeDrawerState(drawerValue: DrawerValue? = null) {
        val isClosed = drawerMenuState.value.drawerValue == DrawerValue.Closed
        drawerMenuState.update {
            it.copy(
                drawerValue = drawerValue ?: if (isClosed) DrawerValue.Open else DrawerValue.Closed
            )
        }
    }
}