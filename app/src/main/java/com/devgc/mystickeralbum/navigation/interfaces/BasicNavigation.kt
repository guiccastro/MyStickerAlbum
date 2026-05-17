package com.devgc.mystickeralbum.navigation.interfaces

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.devgc.mystickeralbum.navigation.NavigationParameters

interface BasicNavigation {

    val routeScreen: String

    fun NavController.navigateToItself(
        parameters: NavigationParameters? = null,
        navOptions: NavOptions? = null
    )
}