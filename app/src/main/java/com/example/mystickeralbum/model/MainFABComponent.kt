package com.example.mystickeralbum.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.mystickeralbum.ui.stateholders.FabUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object MainFABComponent {

    private var fabState: MutableStateFlow<FabUIState> =
        MutableStateFlow(FabUIState())

    @Composable
    fun fabState(): FabUIState {
        return fabState.asStateFlow().collectAsState().value
    }

    fun updateFabState(newState: FabUIState) {
        fabState.value = newState
    }
}