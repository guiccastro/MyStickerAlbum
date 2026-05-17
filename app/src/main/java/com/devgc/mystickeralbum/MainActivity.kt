package com.devgc.mystickeralbum

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.devgc.mystickeralbum.model.ImageCropperHelper
import com.devgc.mystickeralbum.navigation.MainNavComponent.Companion.AppNavHost
import com.devgc.mystickeralbum.scaffold.ui.MainScaffold
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private lateinit var instance: MainActivity

        fun getInstance(): MainActivity {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        setContent {
            MyStickerAlbumTheme {
                MainScaffold {
                    AppNavHost()
                }
            }
        }
    }

    fun cropImage(
        imageUrl: String,
        onResult: (Bitmap) -> Unit
    ) {
        this.registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                if (Build.VERSION.SDK_INT < 28) {
                    onResult(
                        MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            result.uriContent
                        )
                    )
                } else {
                    val source =
                        ImageDecoder.createSource(this.contentResolver, result.uriContent!!)
                    onResult(ImageDecoder.decodeBitmap(source))
                }
            } else {
                val exception = result.error
            }
        }.launch(
            CropImageContractOptions(
                ImageCropperHelper.getOriginalImageUri(imageUrl),
                CropImageOptions()
            )
        )
    }
}