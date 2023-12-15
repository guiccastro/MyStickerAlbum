package com.example.mystickeralbum.navigation.interfaces

import com.example.mystickeralbum.model.FABComponent
import com.example.mystickeralbum.model.TopAppBarComponent

interface Screen : NavigationComponent {

    val topAppBarComponent: TopAppBarComponent?

    val fabComponent: FABComponent?
}