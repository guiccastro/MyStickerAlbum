package com.devgc.mystickeralbum.navigation.interfaces

import androidx.navigation.NavController
import androidx.navigation.NavOptions

interface BasicNavigation {

    val routeScreen: String

    fun NavController.navigateToItself(
        albumName: String? = null,
        navOptions: NavOptions? = null
    )
}