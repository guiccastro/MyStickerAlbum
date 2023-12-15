package com.example.mystickeralbum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mystickeralbum.navigation.MainNavComponent.Companion.AppNavHost
import com.example.mystickeralbum.scaffold.ui.MainScaffold
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyStickerAlbumTheme {
                MainScaffold {
                    AppNavHost()
                }
            }
        }
    }
}