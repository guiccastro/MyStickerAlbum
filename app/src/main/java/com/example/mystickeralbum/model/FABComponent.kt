package com.example.mystickeralbum.model

import androidx.annotation.DrawableRes

interface FABComponent {

    @DrawableRes
    fun getIcon(): Int

    fun onClick() {}
}