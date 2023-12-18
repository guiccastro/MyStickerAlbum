package com.devgc.mystickeralbum.scaffold.stateholders

import androidx.compose.material3.DrawerValue
import com.devgc.mystickeralbum.scaffold.models.DrawerMenuItem

data class DrawerMenuUIState(
    val itemSelected: DrawerMenuItem? = null,
    val drawerValue: DrawerValue = DrawerValue.Closed
)
