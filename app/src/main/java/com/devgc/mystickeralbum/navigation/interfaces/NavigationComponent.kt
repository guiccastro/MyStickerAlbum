package com.devgc.mystickeralbum.navigation.interfaces

import androidx.navigation.NavGraphBuilder

interface NavigationComponent : BasicNavigation {

    fun NavGraphBuilder.screen()
}