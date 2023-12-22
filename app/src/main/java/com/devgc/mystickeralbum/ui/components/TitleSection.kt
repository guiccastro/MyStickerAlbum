package com.devgc.mystickeralbum.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.devgc.mystickeralbum.ui.theme.Poppins

@Composable
fun TitleSection(
    title: String,
    @DrawableRes icon: Int? = null,
    onIconClick: () -> Unit = {},
    color: Color = Color.White,
    fontSize: TextUnit = 20.sp,
    textAlign: TextAlign = TextAlign.Center,
    fontWeight: FontWeight = FontWeight.Bold,
    maxLines: Int = 1
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Divider(
            thickness = 1.dp,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        )

        Text(
            text = title.uppercase(),
            color = color,
            fontSize = fontSize,
            textAlign = textAlign,
            fontWeight = fontWeight,
            maxLines = maxLines,
            style = Poppins
        )

        if (icon != null) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color),
                modifier = Modifier
                    .clickable {
                        onIconClick()
                    }
            )
        }

        Divider(
            thickness = 1.dp,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        )
    }
}

@Preview
@Composable
fun TitleSectionPreview() {
    MyStickerAlbumTheme {
        TitleSection(
            title = "Title",
            icon = R.drawable.ic_about_app
        )
    }
}