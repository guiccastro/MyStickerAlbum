package com.devgc.mystickeralbum.database

import androidx.room.TypeConverter
import com.devgc.mystickeralbum.model.Sticker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

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
}