package com.devgc.mystickeralbum.model

import com.devgc.mystickeralbum.MyStickerAlbumApplication
import com.devgc.mystickeralbum.R

enum class CompoundStickerType {
    LetterNumber,
    NumberLetter;

    fun getTitle(): String {
        return MyStickerAlbumApplication.getInstance().resources.getString(
            when (this) {
                LetterNumber -> R.string.letter_number
                NumberLetter -> R.string.number_letter
            }
        )
    }
}