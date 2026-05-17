package com.devgc.mystickeralbum.ui.stateholders

import androidx.compose.ui.graphics.ImageBitmap
import com.smarttoolfactory.cropper.model.CropAspectRatio

data class CropImageUIState(
    val imageBitmap: ImageBitmap? = null,
    val aspectRatio: Float = 1F,
    val onCropSuccess: (ImageBitmap) -> Unit = {}
)
