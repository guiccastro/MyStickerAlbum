package com.example.mystickeralbum.navigation.screens

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mystickeralbum.model.FABComponent
import com.example.mystickeralbum.navigation.MainNavComponent.Companion.albumNameArgument
import com.example.mystickeralbum.navigation.MainNavComponent.Companion.navController
import com.example.mystickeralbum.R
import com.example.mystickeralbum.navigation.interfaces.Screen
import com.example.mystickeralbum.model.TopAppBarActionItem
import com.example.mystickeralbum.model.TopAppBarComponent
import com.example.mystickeralbum.ui.screens.UpdateAlbumUIScreen
import com.example.mystickeralbum.ui.viewmodels.UpdateAlbumViewModel

object UpdateAlbumScreen : Screen {
    private lateinit var viewModel: UpdateAlbumViewModel

    override val topAppBarComponent: TopAppBarComponent = object : TopAppBarComponent {
        override fun getTitle(): Int = R.string.update_album_title

        override fun hasMenu(): Boolean = false

        override fun hasReturn(): Boolean = true

        override fun getActionItems(): List<TopAppBarActionItem> = listOf(
            TopAppBarActionItem(R.drawable.ic_delete) { viewModel.onDeleteAlbumClick() },
            TopAppBarActionItem(R.drawable.ic_edit) { viewModel.onEditAlbumClick() }
        )
    }

    override val fabComponent: FABComponent? = null

    override fun NavGraphBuilder.screen() {
        composable(
            route = "$routeScreen/{$albumNameArgument}",
            arguments = listOf(navArgument(albumNameArgument) { type = NavType.StringType })
        ) {
            viewModel = hiltViewModel()
            UpdateAlbumUIScreen(viewModel)
        }
    }

    override val routeScreen: String = "UpdateAlbumScreen"

    override fun NavController.navigateToItself(albumName: String?, navOptions: NavOptions?) {
        val screenId = currentBackStack.value.find {
            it.destination.route?.split("/")?.firstOrNull() == routeScreen
        }?.destination?.id
        if (screenId != null) {
            navController.popBackStack(screenId, true)
        }
        navigate("$routeScreen/$albumName", navOptions)
    }
}