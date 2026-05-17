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

    fun getOriginalImage(context: Context, key: String): Bitmap? {
        return getOriginalImageUri(key)?.let { uri ->
            uriToBitmap(context, uri)
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
            .diskCacheKey(url+"edit")
            .allowHardware(false) // Disable hardware bitmaps.
            .build()
    }

    @OptIn(ExperimentalCoilApi::class)
    fun getOriginalImageUri(keyString: String): Uri? {
        val imageLoader = MyStickerAlbumApplication.getInstance().imageLoader
        return imageLoader.diskCache?.openSnapshot(keyString)?.use { snapshot ->
            Uri.fromFile(snapshot.data.toFile())
        }
    }

    fun getCroppedImageUri(context: Context, key: String): Uri? {
        val imageName = getCroppedImageKey(key) + CROPPED_IMAGE_FORMAT
        val file = File(context.cacheDir, imageName)
        return Uri.fromFile(file)
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
//        val imageLoader = MyStickerAlbumApplication.getInstance().imageLoader
//        val imageRequest = getImageRequest(context, bitmap, url)
////        imageLoader.diskCache.openEditor(url)
////        try {
////            FileOutputStream(filename).use { out ->
////                bitmap.compress(
////                    Bitmap.CompressFormat.PNG,
////                    100,
////                    out
////                ) // bmp is your Bitmap instance
////            }
////        } catch (e: IOException) {
////            e.printStackTrace()
////        }
//
//        val result = (imageLoader.execute(imageRequest) as SuccessResult)
//        val Uri = getImageUri("testeedit")
//        Log.println(Log.ASSERT, "Uri", Uri.toString())
//        Log.println(Log.ASSERT, "Result", result.memoryCacheKey.toString())
//        Log.println(Log.ASSERT, "Result", result.isPlaceholderCached.toString())
//        Log.println(Log.ASSERT, "Result", result.diskCacheKey.toString())

        val newPath = context.cacheDir.path
        val imagePath = getImagePath(key)
        val newKey = getCroppedImageKey(key)
        Log.println(Log.ASSERT, "ImagePath", imagePath.toString())
        Log.println(Log.ASSERT, "newPath", newPath)
        Log.println(Log.ASSERT, "newKey", newKey)
        withContext(IO) {
            try {
                val file = File(newPath, "$newKey.jpeg")
                FileOutputStream(file).use { out ->
                    val result = bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        out
                    ) // bmp is your Bitmap instance

                    if (result) {
                        setImage(context, key)
                    }
                    Log.println(Log.ASSERT, "Result", result.toString())
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.println(Log.ASSERT, "Error", e.toString())
            }
        }

    }

    private fun getImageFromDisk(context: Context, key: String): Bitmap? {
        val croppedImageUri = getCroppedImageUri(context, key)
        if (croppedImageUri != null) {
            val file = File(croppedImageUri.path ?: "")
            if (file.exists()) {
                try {
                    return uriToBitmap(context, croppedImageUri)
                } catch (e: Exception) {
                    Log.e("ImageCropperHelper", "Error loading cropped image", e)
                }
            }
        }

        val originalImageUri = getOriginalImageUri(key)
        if (originalImageUri != null) {
            val file = File(originalImageUri.path ?: "")
            if (file.exists()) {
                try {
                    return uriToBitmap(context, originalImageUri)
                } catch (e: Exception) {
                    Log.e("ImageCropperHelper", "Error loading original image", e)
                }
            }
        }

        return null
    }

    private fun uriToBitmap(context: Context, uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
    }

    private fun setImage(context: Context, url: String) {
        val imageDisk = getImageFromDisk(context, url)

        if (imageDisk == null) {
            CoroutineScope(IO).launch {
                try {
                    val bitmap = requestImage(context, url)
                    val newMap = _imageStateHolder.value.hashBitmap.toMutableMap()
                    newMap[url] = bitmap
                    _imageStateHolder.update {
                        ImageStateHolder(
                            HashMap(newMap)
                        )
                    }
                } catch (e: Exception) {
                    Log.e("ImageCropperHelper", "Failed to load image: $url", e)
                }
            }
        } else {
            val newMap = _imageStateHolder.value.hashBitmap.toMutableMap()
            newMap[url] = imageDisk
            _imageStateHolder.update {
                ImageStateHolder(
                    HashMap(newMap)
                )
            }
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
        imageUrl: String,
        launcherForActivityResult: ManagedActivityResultLauncher<CropImageContractOptions, CropImageView.CropResult>
    ) {
        launcherForActivityResult.launch(
            CropImageContractOptions(
                getOriginalImageUri(imageUrl),
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
            if (result.isSuccessful) {
                if (Build.VERSION.SDK_INT < 28) {
                    onResult(
                        MediaStore.Images.Media.getBitmap(
                            context.contentResolver,
                            result.uriContent
                        )
                    )
                } else {
                    val source =
                        ImageDecoder.createSource(context.contentResolver, result.uriContent!!)
                    onResult(ImageDecoder.decodeBitmap(source))
                }
            } else {
                val exception = result.error
                Log.e("ImageCropperHelper", "Crop failed", exception)
            }
        }
    }

//    suspend fun cropImage(
//        context: ComponentActivity,
//        imageUrl: String,
//        onResult: (Bitmap) -> Unit
//    ) {
//        context.registerForActivityResult(CropImageContract()) { result ->
//            if (result.isSuccessful) {
//                if (Build.VERSION.SDK_INT < 28) {
//                    onResult(MediaStore.Images.Media.getBitmap(context.contentResolver, result.uriContent))
//                }
//                else {
//                    val source = ImageDecoder.createSource(context.contentResolver, result.uriContent!!)
//                    onResult(ImageDecoder.decodeBitmap(source))
//                }
//            }
//            else {
//                val exception = result.error
//            }
//        }.launch(
//            CropImageContractOptions(
//                getImageUri(context, urlToBitmap(imageUrl)),
//                CropImageOptions()
//            )
//        )
//    }
}