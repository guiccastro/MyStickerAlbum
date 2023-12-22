package com.devgc.mystickeralbum.model

data class DialogValues(
    val showDialog: Boolean = false,
    val changeDialogState: () -> Unit = {}
)
