package com.devgc.mystickeralbum.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.devgc.mystickeralbum.ui.theme.Poppins

data class TitleSectionIcon(
    @DrawableRes val icon: Int,
    val iconSize: Dp = 24.dp,
    val iconBorderStroke: BorderStroke? = null,
    val iconBorderPadding: Dp = 0.dp,
    val onIconClick: () -> Unit = {},
)

@Composable
fun TitleSection(
    title: String,
    icons: List<TitleSectionIcon> = emptyList(),
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title.uppercase(),
                color = color,
                fontSize = fontSize,
                textAlign = textAlign,
                fontWeight = fontWeight,
                maxLines = maxLines,
                style = Poppins
            )

            icons.forEach { icon ->
                val borderModifier = if (icon.iconBorderStroke != null) Modifier.border(
                    icon.iconBorderStroke,
                    CircleShape
                ) else Modifier
                Image(
                    painter = painterResource(id = icon.icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color),
                    modifier = Modifier
                        .size(icon.iconSize)
                        .clip(CircleShape)
                        .clickable {
                            icon.onIconClick()
                        }
                        .then(borderModifier)
                        .padding(icon.iconBorderPadding)
                )
            }
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
            icons = listOf(
                TitleSectionIcon(
                    icon = R.drawable.ic_about_app
                )
            )
        )
    }
}

@Preview
@Composable
fun TitleSectionPreview2() {
    MyStickerAlbumTheme {
        TitleSection(
            title = "Title",
            icons = listOf(
                TitleSectionIcon(
                    icon = R.drawable.ic_delete,
                    iconSize = 21.dp,
                    iconBorderStroke = BorderStroke(2.dp, Color.White),
                    iconBorderPadding = 3.dp
                ),
                TitleSectionIcon(
                    icon = R.drawable.ic_about_app
                )
            )
        )
    }
}