package com.devgc.mystickeralbum.navigation.interfaces

import com.devgc.mystickeralbum.scaffold.models.FABComponent
import com.devgc.mystickeralbum.scaffold.models.TopAppBarComponent

interface Screen : NavigationComponent {

    val topAppBarComponent: TopAppBarComponent?

    val fabComponent: FABComponent?
}