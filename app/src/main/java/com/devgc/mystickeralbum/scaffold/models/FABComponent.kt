package com.devgc.mystickeralbum.scaffold.models

import androidx.annotation.DrawableRes

interface FABComponent {

    @DrawableRes
    fun getIcon(): Int

    fun onClick() {}
}