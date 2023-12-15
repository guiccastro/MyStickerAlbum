package com.example.mystickeralbum.ui.stateholders

import androidx.compose.material3.DrawerValue
import com.example.mystickeralbum.model.DrawerMenuItem

data class DrawerMenuUIState(
    val itemSelected: DrawerMenuItem? = null,
    val drawerValue: DrawerValue = DrawerValue.Closed
)
