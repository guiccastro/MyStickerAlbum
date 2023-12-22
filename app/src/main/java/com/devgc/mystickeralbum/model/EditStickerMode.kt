package com.devgc.mystickeralbum.model

import android.content.Context
import androidx.annotation.StringRes
import com.devgc.mystickeralbum.R

enum class EditStickerMode {
    AddStickers,
    RemoveStickers;

    @StringRes
    fun getTitle(): Int {
        return when (this) {
            AddStickers -> R.string.add_stickers
            RemoveStickers -> R.string.remove_stickers
        }
    }

    companion object {
        fun getOptionsString(context: Context): List<String> {
            val resources = context.resources
            return values().map { resources.getString(it.getTitle()) }
        }

        fun getByIndex(index: Int): EditStickerMode {
            return values()[index]
        }
    }
}