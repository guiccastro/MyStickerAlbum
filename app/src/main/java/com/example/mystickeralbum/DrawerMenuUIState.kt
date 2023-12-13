package com.example.mystickeralbum

import androidx.compose.material3.DrawerValue

data class DrawerMenuUIState(
    val itemSelected: DrawerMenuItem? = null,
    val drawerValue: DrawerValue = DrawerValue.Closed
)
