package com.devgc.mystickeralbum.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DecimalFormat

@Entity
data class Album(
    @PrimaryKey val name: String,
    @Embedded val stickersList: StickersList,
    val status: AlbumStatus,
    val albumImage: String
) {
    fun getMissing(): List<Sticker> {
        return stickersList.stickers.filter { !it.found }
    }

    fun getFound(): List<Sticker> {
        return stickersList.stickers.filter { it.found }
    }

    fun getRepeated(): List<Sticker> {
        val repeated = ArrayList<Sticker>()
        stickersList.stickers.filter { it.repeated > 0 }.forEach { sticker ->
            repeat(sticker.repeated) {
                repeated.add(sticker)
            }
        }
        return repeated
    }

    fun getProgress(): Float {
        val total = getTotalStickers().toFloat()
        val found = getFound().size.toFloat()
        val result = found / total

        return if (result.isNaN()) 0F else result
    }

    fun getFormattedProgress(): String {
        val progress = getProgress() * 100
        return DecimalFormat("###").format(progress) + "%"
    }

    fun getTotalStickers(): Int {
        return stickersList.stickers.size
    }
}
