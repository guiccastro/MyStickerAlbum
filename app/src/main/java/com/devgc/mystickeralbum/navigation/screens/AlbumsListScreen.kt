package com.devgc.mystickeralbum.navigation.screens

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.navigation.interfaces.Screen
import com.devgc.mystickeralbum.scaffold.models.FABComponent
import com.devgc.mystickeralbum.scaffold.models.TopAppBarActionItem
import com.devgc.mystickeralbum.scaffold.models.TopAppBarComponent
import com.devgc.mystickeralbum.ui.screens.AlbumsListUIScreen
import com.devgc.mystickeralbum.ui.viewmodels.AlbumsListViewModel

object AlbumsListScreen : Screen {
    lateinit var viewModel: AlbumsListViewModel

    override val topAppBarComponent: TopAppBarComponent = object : TopAppBarComponent {
        override fun getTitle(): Int = R.string.album_list_title

        override fun hasMenu(): Boolean = true

        override fun hasReturn(): Boolean = false

        override fun getActionItems(): List<TopAppBarActionItem> = listOf(
            TopAppBarActionItem(R.drawable.ic_about_app) { viewModel.changeIconsLegendDialogState() }
        )
    }

    override val fabComponent: FABComponent = object : FABComponent {
        override fun getIcon(): Int = R.drawable.ic_add

        override fun onClick() {
            super.onClick()
            viewModel.onFabClick()
        }
    }

    override fun NavGraphBuilder.screen() {
        composable(routeScreen) {
            viewModel = hiltViewModel()
            AlbumsListUIScreen(viewModel)
        }
    }

    override val routeScreen: String = "AlbumsListScreen"

    override fun NavController.navigateToItself(albumName: String?, navOptions: NavOptions?) =
        navigate(routeScreen, navOptions)
}