package com.example.mystickeralbum.navigation.interfaces

import com.example.mystickeralbum.scaffold.models.FABComponent
import com.example.mystickeralbum.scaffold.models.TopAppBarComponent

interface Screen : NavigationComponent {

    val topAppBarComponent: TopAppBarComponent?

    val fabComponent: FABComponent?
}