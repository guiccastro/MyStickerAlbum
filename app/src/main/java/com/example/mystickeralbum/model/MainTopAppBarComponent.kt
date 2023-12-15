package com.example.mystickeralbum.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.mystickeralbum.ui.stateholders.TopAppBarUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object MainTopAppBarComponent {

    private var topAppBarState: MutableStateFlow<TopAppBarUIState> =
        MutableStateFlow(TopAppBarUIState())

    @Composable
    fun topAppBarState(): TopAppBarUIState {
        return topAppBarState.asStateFlow().collectAsState().value
    }

    fun updateTopAppBarState(newState: TopAppBarUIState) {
        topAppBarState.value = newState
    }
}