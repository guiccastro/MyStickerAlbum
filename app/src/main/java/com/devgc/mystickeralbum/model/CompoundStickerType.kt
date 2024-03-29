package com.devgc.mystickeralbum.model

import android.content.Context
import androidx.annotation.StringRes
import com.devgc.mystickeralbum.R

enum class CompoundStickerType {
    LetterNumber,
    NumberLetter;

    @StringRes
    fun getTitle(): Int {
        return when (this) {
            LetterNumber -> R.string.letter_number
            NumberLetter -> R.string.number_letter
        }
    }

    companion object {
        fun getOptionsString(context: Context): List<String> {
            val resources = context.resources
            return values().map { resources.getString(it.getTitle()) }
        }

        fun getByIndex(index: Int): CompoundStickerType {
            return values()[index]
        }
    }
}