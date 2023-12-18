package com.devgc.mystickeralbum.scaffold.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devgc.mystickeralbum.scaffold.stateholders.FabUIState

@Composable
fun FabApp(state: FabUIState) {
    if (state.hasFab) {
        SmallFloatingActionButton(
            onClick = { state.onClick() },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(50.dp)
        ) {
            Icon(
                painter = painterResource(id = state.icon),
                contentDescription = null
            )
        }
    }
}