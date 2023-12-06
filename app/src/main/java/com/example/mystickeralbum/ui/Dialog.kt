package com.example.mystickeralbum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mystickeralbum.R
import com.example.mystickeralbum.model.ButtonItem
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun SimpleDialog(
    title: String? = null,
    description: String? = null,
    negativeButton: ButtonItem? = null,
    positiveButton: ButtonItem? = null
) {
    Dialog(onDismissRequest = { negativeButton?.onClick?.invoke() }) {
        Column(
            modifier = Modifier
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
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
                            containerColor = Color.Gray
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

