package com.example.mystickeralbum

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    floatingActionButton: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        floatingActionButton = { floatingActionButton() },
        topBar = { topBar() }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}