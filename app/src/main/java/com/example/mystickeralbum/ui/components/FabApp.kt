package com.example.mystickeralbum.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.mystickeralbum.ui.stateholders.FabUIState

@Composable
fun FabApp(state: FabUIState) {
    if (state.hasFab) {
        SmallFloatingActionButton(
            onClick = { state.onClick() },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                painter = painterResource(id = state.icon),
                contentDescription = null
            )
        }
    }
}