package com.devgc.mystickeralbum.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.model.ButtonItem
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun SimpleDialog(
    title: String? = null,
    description: String? = null,
    descriptionLineHeight: TextUnit = TextUnit.Unspecified,
    negativeButton: ButtonItem? = null,
    positiveButton: ButtonItem? = null
) {
    BaseDialog(
        onDismissRequest = { negativeButton?.onClick?.invoke() }
    ) {
        if (title != null) {
            Text(
                text = title.uppercase(),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        if (description != null) {
            Text(
                text = description,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = descriptionLineHeight,
                color = Color.White.copy(alpha = 0.82F)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (negativeButton?.text != null) {
                Button(
                    onClick = { negativeButton.onClick() },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.14F),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = negativeButton.text)
                }
            }

            if (positiveButton?.text != null) {
                Button(
                    onClick = { positiveButton.onClick() },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1D7BE0),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = positiveButton.text)
                }
            }
        }
    }
}

@Composable
fun BaseDialog(
    onDismissRequest: () -> Unit = {},
    backgroundColor: Color = Color(0xFF0B2D4A),
    shape: Shape = RoundedCornerShape(18.dp),
    paddingValues: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(20.dp),
    content: @Composable (ColumnScope.() -> Unit)
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .background(backgroundColor, shape)
                .padding(paddingValues),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement
        ) {
            content()
        }
    }
}

@Composable
fun IconsLegendDialog(onDismissRequest: () -> Unit) {
    val isTablet = booleanResource(id = R.bool.isTablet)
    val titleSize = if (isTablet) 24.sp else 16.sp
    val dividerWidth = if (isTablet) 250.dp else 180.dp
    BaseDialog(
        onDismissRequest = onDismissRequest,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.icons_legend_dialog_title).uppercase(),
            fontSize = titleSize,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        IconLegend(
            icon = R.drawable.ic_stat_total,
            legend = R.string.album_item_total
        )

        HorizontalDivider(
            modifier = Modifier
                .width(dividerWidth),
            thickness = (0.5).dp,
            color = Color.White.copy(alpha = 0.18F)
        )

        IconLegend(
            icon = R.drawable.ic_stat_owned,
            legend = R.string.album_item_owned
        )

        HorizontalDivider(
            modifier = Modifier
                .width(dividerWidth),
            thickness = (0.5).dp,
            color = Color.White.copy(alpha = 0.18F)
        )

        IconLegend(
            icon = R.drawable.ic_stat_missing,
            legend = R.string.album_item_missing
        )

        HorizontalDivider(
            modifier = Modifier
                .width(dividerWidth),
            thickness = (0.5).dp,
            color = Color.White.copy(alpha = 0.18F)
        )

        IconLegend(
            icon = R.drawable.ic_stat_repeated,
            legend = R.string.album_item_repeated
        )
    }
}

@Composable
fun IconLegend(
    @DrawableRes icon: Int,
    @StringRes legend: Int
) {
    val isTablet = booleanResource(id = R.bool.isTablet)
    val rowWidth = if (isTablet) 250.dp else 180.dp
    val iconSize = if (isTablet) 40.dp else 30.dp
    val legendSize = if (isTablet) 24.sp else 18.sp
    val legendPadding = if (isTablet) 20.dp else 10.dp
    Row(
        modifier = Modifier
            .width(rowWidth),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(iconSize)
                .aspectRatio(1F),
            colorFilter = ColorFilter.tint(Color.White)
        )

        Text(
            text = stringResource(id = legend),
            fontSize = legendSize,
            modifier = Modifier
                .weight(1F)
                .padding(start = legendPadding),
            textAlign = TextAlign.Center,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun SimpleDialogPreview() {
    MyStickerAlbumTheme {
        SimpleDialog(
            title = stringResource(id = R.string.album_list_title),
            description = stringResource(id = R.string.album_list_empty),
            negativeButton = ButtonItem(text = stringResource(id = R.string.cancel_button)) {},
            positiveButton = ButtonItem(text = stringResource(id = R.string.create_button)) {},
        )
    }
}

@Preview
@Composable
fun IconsLegendDialogPreview() {
    MyStickerAlbumTheme {
        IconsLegendDialog(
            onDismissRequest = {}
        )
    }
}

