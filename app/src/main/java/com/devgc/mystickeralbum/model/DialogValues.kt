package com.devgc.mystickeralbum.model

data class DialogValues(
    val showDialog: Boolean = false,
    val changeDialogState: () -> Unit = {},
    val changeDialogStateValue: (Any) -> Unit = {},
    val value: Any? = null
)
