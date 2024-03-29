package com.devgc.mystickeralbum.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.devgc.mystickeralbum.model.ToggleGroupValues
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme
import com.devgc.mystickeralbum.ui.theme.Poppins

@Composable
fun ToggleGroup(
    toggleGroupValues: ToggleGroupValues,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight = FontWeight.Normal,
    selectedFontWeight: FontWeight = FontWeight.Medium,
    cornerRadius: Dp = 8.dp,
    borderWidth: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    selectedColorBorder: Color = MaterialTheme.colorScheme.tertiary,
    background: Color = MaterialTheme.colorScheme.primaryContainer,
    selectedBackground: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Row {
        toggleGroupValues.options.forEachIndexed { index, option ->
            OutlinedButton(
                onClick = { toggleGroupValues.onOptionClick(index) },
                shape = when (index) {
                    // left outer button
                    0 -> RoundedCornerShape(
                        topStart = cornerRadius,
                        topEnd = 0.dp,
                        bottomStart = cornerRadius,
                        bottomEnd = 0.dp
                    )
                    // right outer button
                    toggleGroupValues.options.size - 1 -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = cornerRadius,
                        bottomStart = 0.dp,
                        bottomEnd = cornerRadius
                    )
                    // middle button
                    else -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                },
                border = BorderStroke(
                    borderWidth, if (toggleGroupValues.selectedIndex == index) {
                        selectedColorBorder
                    } else {
                        borderColor
                    }
                ),
                colors = if (toggleGroupValues.selectedIndex == index) {
                    // selected colors
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = selectedBackground,
                        contentColor = selectedContentColor
                    )
                } else {
                    // not selected colors
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = background,
                        contentColor = contentColor
                    )
                },
                modifier = Modifier
                    .weight(1F, false)
            ) {
                Text(
                    text = option,
                    color = if (toggleGroupValues.selectedIndex == index) {
                        selectedContentColor
                    } else {
                        contentColor
                    },
                    style = Poppins,
                    fontSize = fontSize,
                    fontWeight = if (toggleGroupValues.selectedIndex == index) selectedFontWeight else fontWeight,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ToggleGroupPreview() {
    MyStickerAlbumTheme {

        Column {
            ToggleGroup(
                toggleGroupValues = ToggleGroupValues(
                    options = listOf("Option1", "Option2", "Option3"),
                    onOptionClick = {},
                    selectedIndex = 0
                )
            )
            ToggleGroup(
                toggleGroupValues = ToggleGroupValues(
                    options = listOf("Option1", "Option2", "Option3"),
                    onOptionClick = {},
                    selectedIndex = 1
                )
            )
            ToggleGroup(
                toggleGroupValues = ToggleGroupValues(
                    options = listOf("Option1", "Option2", "Option3"),
                    onOptionClick = {},
                    selectedIndex = 2
                )
            )
        }
    }
}