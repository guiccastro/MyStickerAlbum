package com.example.mystickeralbum.navigation

import android.os.Bundle
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.mystickeralbum.navigation.interfaces.Screen
import com.example.mystickeralbum.navigation.screens.AboutAppScreen
import com.example.mystickeralbum.navigation.screens.AlbumsListScreen
import com.example.mystickeralbum.navigation.screens.CreateAlbumScreen
import com.example.mystickeralbum.navigation.screens.EditAlbumScreen
import com.example.mystickeralbum.navigation.screens.LanguageScreen
import com.example.mystickeralbum.navigation.screens.SettingsScreen
import com.example.mystickeralbum.navigation.screens.UpdateAlbumScreen
import com.example.mystickeralbum.scaffold.maincomponents.MainDrawerMenuComponent
import com.example.mystickeralbum.scaffold.maincomponents.MainDrawerMenuComponent.updateDrawerMenuState
import com.example.mystickeralbum.scaffold.maincomponents.MainFABComponent.updateFabState
import com.example.mystickeralbum.scaffold.maincomponents.MainTopAppBarComponent.updateTopAppBarState
import com.example.mystickeralbum.scaffold.stateholders.DrawerMenuUIState
import com.example.mystickeralbum.scaffold.stateholders.FabUIState
import com.example.mystickeralbum.scaffold.stateholders.TopAppBarUIState

class MainNavComponent private constructor() {

    private lateinit var _navController: NavHostController

    companion object : NavController.OnDestinationChangedListener {

        @Volatile
        private var instance: MainNavComponent? = null

        private fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: MainNavComponent().also { instance = it }
            }

        private val screensList = listOf(
            AlbumsListScreen,
            CreateAlbumScreen,
            EditAlbumScreen,
            UpdateAlbumScreen,
            SettingsScreen,
            LanguageScreen,
            AboutAppScreen
        )

        val navController: NavHostController get() = getInstance()._navController

        const val albumNameArgument = "albumName"

        @Composable
        fun AppNavHost() {
            getInstance()._navController = rememberNavController()

            LaunchedEffect(Unit) {
                navController.addOnDestinationChangedListener(this@Companion)
            }

            NavHost(
                navController = navController,
                startDestination = AlbumsListScreen.routeScreen,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) {
                screensList.forEach {
                    it.apply {
                        screen()
                    }
                }
            }
        }

        override fun onDestinationChanged(
            controller: NavController,
            destination: NavDestination,
            arguments: Bundle?
        ) {
            val route = destination.route?.split("/")?.first() ?: ""
            val screen = getScreen(route)

            updateTopBar(screen)
            updateFAB(screen)
            updateDrawerMenu(screen)
        }

        private fun updateTopBar(screen: Screen?) {
            val topAppBarComponent = screen?.topAppBarComponent
            updateTopAppBarState(
                topAppBarComponent?.let {
                    TopAppBarUIState(
                        title = topAppBarComponent.getTitle(),
                        hasMenu = topAppBarComponent.hasMenu(),
                        hasReturn = topAppBarComponent.hasReturn(),
                        onClickReturn = { navController.popBackStack() },
                        actionItems = topAppBarComponent.getActionItems()
                    )
                } ?: run {
                    TopAppBarUIState()
                }
            )
        }

        private fun updateFAB(screen: Screen?) {
            val fabComponent = screen?.fabComponent
            updateFabState(
                fabComponent?.let {
                    FabUIState(
                        icon = fabComponent.getIcon(),
                        hasFab = true,
                        onClick = fabComponent::onClick
                    )
                } ?: run {
                    FabUIState()
                }
            )
        }

        private fun updateDrawerMenu(screen: Screen?) {
            val drawerItemSelected = MainDrawerMenuComponent.findItemByScreen(screen)
            updateDrawerMenuState(
                DrawerMenuUIState(
                    itemSelected = drawerItemSelected
                )
            )
        }

        fun getAllScreens(): List<Screen> {
            val screens = ArrayList<Screen>()
            screens.addAll(screensList)
            return screens
        }

        fun getScreen(route: String): Screen? {
            return getAllScreens().find {
                it.routeScreen == route
            }
        }

        fun getSingleTopWithPopUpTo(route: String, isInclusive: Boolean = false): NavOptions {
            return navOptions {
                launchSingleTop = true
                popUpTo(route) { inclusive = isInclusive }
            }
        }
    }
}