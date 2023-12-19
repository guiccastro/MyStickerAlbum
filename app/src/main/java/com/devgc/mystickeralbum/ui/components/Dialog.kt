package com.devgc.mystickeralbum.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
                fontWeight = FontWeight.SemiBold
            )
        }

        if (description != null) {
            Text(
                text = description,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (negativeButton != null) {
                Button(
                    onClick = { negativeButton.onClick() },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(text = negativeButton.text)
                }
            }

            if (positiveButton != null) {
                Button(
                    onClick = { positiveButton.onClick() },
                    shape = RoundedCornerShape(4.dp)
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
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    shape: Shape = RoundedCornerShape(8.dp),
    paddingValues: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
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

