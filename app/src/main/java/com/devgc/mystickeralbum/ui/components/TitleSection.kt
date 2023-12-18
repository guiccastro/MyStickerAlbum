package com.devgc.mystickeralbum.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleSection(
    title: String,
    color: Color = Color.White,
    fontSize: TextUnit = 20.sp,
    textAlign: TextAlign = TextAlign.Center,
    fontWeight: FontWeight = FontWeight.Bold,
    maxLines: Int = 1
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            thickness = 1.dp,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        )

        Text(
            text = "  " + title.uppercase() + "  ",
            color = color,
            fontSize = fontSize,
            textAlign = textAlign,
            fontWeight = fontWeight,
            maxLines = maxLines
        )

        Divider(
            thickness = 1.dp,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        )
    }
}