package com.devgc.mystickeralbum.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import coil.annotation.ExperimentalCoilApi
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.devgc.mystickeralbum.MyStickerAlbumApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.Path
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


data class ImageStateHolder(
    val hashBitmap: HashMap<String, Bitmap?> = HashMap()
)

object ImageCropperHelper {

    private val _imageStateHolder: MutableStateFlow<ImageStateHolder> =
        MutableStateFlow(ImageStateHolder())
    val imageStateHolder get() = _imageStateHolder.asStateFlow()

    const val imageCacheModified = "image_cache_modified"

    const val CROPPED_IMAGE_FORMAT = ".jpeg"

    @Composable
    fun ImageStateHolder.getImageState(context: Context, key: String): Bitmap? {
        val hashMapValue = hashBitmap[key]

        if (hashMapValue == null) {
            setImage(context, key)
        }

        return hashMapValue
    }

    fun getImage(key: String): Bitmap? {
        return _imageStateHolder.value.hashBitmap[key]
    }

    @OptIn(ExperimentalCoilApi::class)
    fun getOriginalImage(context: Context, key: String): Bitmap? {
        val imageLoader = MyStickerAlbumApplication.getInstance().imageLoader
        return imageLoader.diskCache?.openSnapshot(key)?.use { snapshot ->
            uriToBitmap(context, Uri.fromFile(snapshot.data.toFile()))
        }
    }

    private fun getImageRequest(context: Context, url: String): ImageRequest {
        return ImageRequest.Builder(context)
            .diskCachePolicy(CachePolicy.ENABLED)
            .data(url)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()
    }

    private fun getImageRequest(context: Context, bitmap: Bitmap, url: String): ImageRequest {
        return ImageRequest.Builder(context)
            .diskCachePolicy(CachePolicy.ENABLED)
            .data(bitmap)
            .diskCacheKey(url + "edit")
            .allowHardware(false) // Disable hardware bitmaps.
            .build()
    }

    @OptIn(ExperimentalCoilApi::class)
    fun getOriginalImageUri(context: Context, keyString: String): Uri? {
        val imageLoader = MyStickerAlbumApplication.getInstance().imageLoader
        return imageLoader.diskCache?.openSnapshot(keyString)?.use { snapshot ->
            try {
                val tempFile = File(context.cacheDir, "temp_" + getCroppedImageKey(keyString) + CROPPED_IMAGE_FORMAT)
                snapshot.data.toFile().copyTo(tempFile, overwrite = true)
                Uri.fromFile(tempFile)
            } catch (e: Exception) {
                Log.e("ImageCropperHelper", "Error copying original image to temp file", e)
                null
            }
        }
    }

    fun getCroppedImageUri(context: Context, key: String): Uri? {
        val imageName = getCroppedImageKey(key) + CROPPED_IMAGE_FORMAT
        val file = File(context.cacheDir, imageName)
        return if (file.exists()) Uri.fromFile(file) else null
    }

    @OptIn(ExperimentalCoilApi::class)
    fun getImagePath(keyString: String): Path? {
        val imageLoader = MyStickerAlbumApplication.getInstance().imageLoader
        return imageLoader.diskCache?.openSnapshot(keyString)?.use { it.data }
    }

    private fun getCroppedImageKey(originalKey: String): String {
        return originalKey.filter { it.isLetter() }
    }

    suspend fun saveImage(context: Context, bitmap: Bitmap, key: String) {
        val newPath = context.cacheDir.path
        val newKey = getCroppedImageKey(key)
        withContext(IO) {
            try {
                val file = File(newPath, "$newKey.jpeg")
                FileOutputStream(file).use { out ->
                    val result = bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        out
                    )

                    if (result) {
                        setImage(context, key)
                    }
                }
            } catch (e: IOException) {
                Log.e("ImageCropperHelper", "Error saving image", e)
            }
        }
    }

    private fun getImageFromDisk(context: Context, key: String): Bitmap? {
        val croppedImageUri = getCroppedImageUri(context, key)
        if (croppedImageUri != null) {
            val bitmap = uriToBitmap(context, croppedImageUri)
            if (bitmap != null) return bitmap
        }

        return getOriginalImage(context, key)
    }

    private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT < 28) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = if (uri.scheme == "file" && uri.path != null) {
                    ImageDecoder.createSource(File(uri.path!!))
                } else {
                    ImageDecoder.createSource(context.contentResolver, uri)
                }
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            Log.e("ImageCropperHelper", "Error converting uri to bitmap: $uri", e)
            null
        }
    }

    private val loadingKeys = mutableSetOf<String>()

    private fun setImage(context: Context, url: String) {
        synchronized(loadingKeys) {
            if (loadingKeys.contains(url)) return
            loadingKeys.add(url)
        }

        CoroutineScope(IO).launch {
            try {
                val imageDisk = getImageFromDisk(context, url)

                if (imageDisk == null) {
                    try {
                        val bitmap = requestImage(context, url)
                        updateImageState(url, bitmap)
                    } catch (e: Exception) {
                        Log.e("ImageCropperHelper", "Failed to load image: $url", e)
                    }
                } else {
                    updateImageState(url, imageDisk)
                }
            } finally {
                synchronized(loadingKeys) {
                    loadingKeys.remove(url)
                }
            }
        }
    }

    private fun updateImageState(url: String, bitmap: Bitmap) {
        _imageStateHolder.update { state ->
            val newMap = state.hashBitmap.toMutableMap()
            newMap[url] = bitmap
            ImageStateHolder(HashMap(newMap))
        }
    }

    private suspend fun requestImage(context: Context, url: String): Bitmap {
        val loader = MyStickerAlbumApplication.getInstance().imageLoader
        val request = getImageRequest(context, url)
        val resultReq = loader.execute(request)
        if (resultReq is SuccessResult) {
            return (resultReq.drawable as BitmapDrawable).bitmap
        } else {
            throw IOException("Failed to download image: $url")
        }
    }

    suspend fun cropImage(
        context: Context,
        imageUrl: String,
        launcherForActivityResult: ManagedActivityResultLauncher<CropImageContractOptions, CropImageView.CropResult>
    ) {
        launcherForActivityResult.launch(
            CropImageContractOptions(
                getOriginalImageUri(context, imageUrl),
                CropImageOptions()
            )
        )
    }

    @Composable
    fun getLauncherForActivityResult(
        context: Context,
        onResult: (Bitmap) -> Unit
    ): ManagedActivityResultLauncher<CropImageContractOptions, CropImageView.CropResult> {
        return rememberLauncherForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful && result.uriContent != null) {
                CoroutineScope(IO).launch {
                    val bitmap = uriToBitmap(context, result.uriContent!!)
                    if (bitmap != null) {
                        withContext(Main) {
                            onResult(bitmap)
                        }
                    }
                }
            } else {
                val exception = result.error
                Log.e("ImageCropperHelper", "Crop failed", exception)
            }
        }
    }
}
