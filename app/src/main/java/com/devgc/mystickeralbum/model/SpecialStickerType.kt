package com.devgc.mystickeralbum.model

import androidx.annotation.StringRes
import com.devgc.mystickeralbum.R

enum class SpecialStickerType {
    LetterNumber,
    NumberLetter;

    @StringRes
    fun getTitle(): Int {
        return when (this) {
            LetterNumber -> R.string.letter_number
            NumberLetter -> R.string.number_letter
        }
    }
}