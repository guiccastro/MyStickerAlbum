package com.devgc.mystickeralbum.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun TextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(all = 0.dp),
    placeholderText: String = "",
    textStyle: TextStyle = TextStyle.Default,
    textSize: TextUnit = 10.sp,
    textColor: Color = Color.Black,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = RoundedCornerShape(4.dp),
    borderWidth: Dp = 1.dp,
    borderColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    error: Boolean = false,
    errorBorderColor: Color = Color.Red,
    errorMessage: String = "",
    errorModifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
    ) {
        Box {
            BasicTextField(
                value = text,
                onValueChange = {
                    onValueChange(it)
                },
                textStyle = textStyle.copy(fontSize = textSize, color = textColor),
                singleLine = true,
                modifier = modifier
                    .border(borderWidth, if (error) errorBorderColor else borderColor, shape)
                    .background(backgroundColor, shape),
                keyboardOptions = keyboardOptions,
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (leadingIcon != null) leadingIcon()
                        Box(Modifier.weight(1f)) {
                            if (text.isEmpty()) Text(
                                placeholderText,
                                style = textStyle.copy(
                                    fontSize = textSize,
                                    color = textColor.copy(alpha = 0.8F)
                                )
                            )
                            innerTextField()
                        }
                        if (trailingIcon != null) trailingIcon()
                    }
                },
                keyboardActions = keyboardActions
            )
        }

        if (error) {
            Text(
                text = errorMessage,
                fontSize = (textSize.value - 4.sp.value).sp,
                color = Color.Red,
                modifier = errorModifier,
                lineHeight = (textSize.value - 4.sp.value).sp
            )
        }
    }
}

@Preview
@Composable
fun TextFieldPreview() {
    MyStickerAlbumTheme {
        TextField(
            text = "Teste",
            onValueChange = {},
            error = true,
            errorMessage = "Error Error Error Error Error",
            errorModifier = Modifier
                .width(40.dp)
        )
    }
}