package com.devgc.mystickeralbum.navigation.screens

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devgc.mystickeralbum.navigation.MainNavComponent
import com.devgc.mystickeralbum.navigation.NavigationParameters
import com.devgc.mystickeralbum.navigation.interfaces.Screen
import com.devgc.mystickeralbum.scaffold.models.FABComponent
import com.devgc.mystickeralbum.scaffold.models.TopAppBarComponent
import com.devgc.mystickeralbum.ui.screens.CropImageUIScreen
import com.devgc.mystickeralbum.ui.viewmodels.CropImageViewModel

object CropImageScreen : Screen {
    override val topAppBarComponent: TopAppBarComponent? = null
    override val fabComponent: FABComponent? = null

    override fun NavGraphBuilder.screen() {
        composable(
            route = "${routeScreen}/{${MainNavComponent.albumNameArgument}}/{${MainNavComponent.aspectRatioArgument}}",
            arguments = listOf(
                navArgument(MainNavComponent.albumNameArgument) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(MainNavComponent.aspectRatioArgument) {
                    type = NavType.FloatType
                    nullable = false
                }
            )
        ) {
            val viewModel: CropImageViewModel = hiltViewModel()
            CropImageUIScreen(viewModel)
        }
    }

    override val routeScreen: String = "CropImageScreen"

    override fun NavController.navigateToItself(
        parameters: NavigationParameters?,
        navOptions: NavOptions?
    ) =
        navigate("$routeScreen/${parameters?.albumName}/${parameters?.aspectRatio}", navOptions)
}