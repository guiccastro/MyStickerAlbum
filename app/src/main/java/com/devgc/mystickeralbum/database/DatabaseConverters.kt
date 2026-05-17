package com.devgc.mystickeralbum.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Log
import androidx.room.TypeConverter
import com.devgc.mystickeralbum.model.Sticker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.nio.ByteBuffer

class DatabaseConverters {

    @TypeConverter
    fun stickersToString(stickers: List<Sticker>): String {
        return Gson().toJson(stickers)
    }

    @TypeConverter
    fun stringToStickers(string: String): List<Sticker> {
        val listType: Type = object : TypeToken<ArrayList<Sticker>>() {}.type
        return Gson().fromJson(string, listType)
    }

    @TypeConverter
    fun bitmapToBase64(bitmap: Bitmap?): String {
        return if (bitmap != null) {
            // create a ByteBuffer and allocate size equal to bytes in   the bitmap
            val byteBuffer = ByteBuffer.allocate(bitmap.height * bitmap.rowBytes)
            //copy all the pixels from bitmap to byteBuffer
            bitmap.copyPixelsToBuffer(byteBuffer)
            //convert byte buffer into byteArray
            val byteArray = byteBuffer.array()
            //convert byteArray to Base64 String with default flags
            Base64.encodeToString(byteArray, DEFAULT)
        } else {
            ""
        }
    }

    @TypeConverter
    fun base64ToBitmap(base64String: String): Bitmap? {
        return if (base64String.isNotEmpty()) {
            //convert Base64 String into byteArray
            val byteArray = Base64.decode(base64String, DEFAULT)
            //byteArray to Bitmap
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            Log.println(Log.ASSERT, "Room_bitmap", bitmap.toString())
            bitmap
        } else {
            null
        }
    }
}