package com.devgc.mystickeralbum.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.ui.stateholders.CropImageUIState
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.devgc.mystickeralbum.ui.viewmodels.CropImageViewModel
import com.smarttoolfactory.cropper.ImageCropper
import com.smarttoolfactory.cropper.model.AspectRatio
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.RectCropShape
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import com.smarttoolfactory.cropper.settings.CropProperties
import com.smarttoolfactory.cropper.settings.CropStyle
import com.smarttoolfactory.cropper.settings.CropType

@Composable
fun CropImageUIScreen(viewModel: CropImageViewModel) {
    val state = viewModel.uiState.collectAsState().value
    CropImageUIScreen(state)
}

@Composable
fun CropImageUIScreen(state: CropImageUIState) {
    MainContent(
        imageBitmap = state.imageBitmap,
        cropProperties = CropDefaults.properties(
            cropType = CropType.Dynamic,
            aspectRatio = AspectRatio(state.aspectRatio),
            fixedAspectRatio = true,
            overlayRatio = 1F,
            handleSize = 50.dp.value,
            cropOutlineProperty = CropOutlineProperty(
                outlineType = OutlineType.ImageMask,
                cropOutline = RectCropShape(1, "RectCropShape")
            )
        ),
        cropStyle = CropDefaults.style(),
        onCropSuccess = state.onCropSuccess
    )
}

@Composable
private fun MainContent(
    imageBitmap: ImageBitmap?,
    cropProperties: CropProperties,
    cropStyle: CropStyle,
    onCropSuccess: (ImageBitmap) -> Unit
) {
    var crop by remember { mutableStateOf(false) }
    var isCropping by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF071827),
                        Color(0xFF0B2D4A),
                        Color(0xFF071420)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            imageBitmap?.let {
                ImageCropper(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    imageBitmap = it,
                    contentDescription = null,
                    filterQuality = FilterQuality.High,
                    cropStyle = cropStyle,
                    cropProperties = cropProperties,
                    crop = crop,
                    onCropStart = {
                        isCropping = true
                    },
                    onCropSuccess = { imageResult ->
                        isCropping = false
                        crop = false

                        onCropSuccess(imageResult)
                    }
                )
            }
        }

        Button(
            onClick = { crop = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1D7BE0),
                contentColor = Color.White
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null
            )
        }

        if (isCropping) {
            CircularProgressIndicator(
                color = Color(0xFF37D4B5),
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CropImageUIScreenPreview() {
    MyStickerAlbumTheme {
        CropImageUIScreen(
            state = CropImageUIState()
        )
    }
}
