package com.example.mystickeralbum.model

import java.text.DecimalFormat

data class Album(
    val name: String,
    val stickers: List<Sticker>,
    val status: AlbumStatus,
    val albumImage: String
) {
    fun getMissing(): List<Sticker> {
        return stickers.filter { !it.found }
    }

    fun getFound(): List<Sticker> {
        return stickers.filter { it.found }
    }

    fun getRepeated(): List<Sticker> {
        val repeated = ArrayList<Sticker>()
        stickers.filter { it.repeated > 0 }.forEach { sticker ->
            repeat(sticker.repeated) {
                repeated.add(sticker)
            }
        }
        return repeated
    }

    fun getProgress(): Float {
        val total = getTotalStickers().toFloat()
        val found = getFound().size.toFloat()
        return found / total
    }

    fun getFormattedProgress(): String {
        val progress = getProgress() * 100
        return DecimalFormat("###").format(progress) + "%"
    }

    fun getTotalStickers(): Int {
        return stickers.size
    }
}
