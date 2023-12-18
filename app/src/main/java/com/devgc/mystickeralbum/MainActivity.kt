package com.devgc.mystickeralbum

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.devgc.mystickeralbum.navigation.MainNavComponent.Companion.AppNavHost
import com.devgc.mystickeralbum.scaffold.ui.MainScaffold
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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