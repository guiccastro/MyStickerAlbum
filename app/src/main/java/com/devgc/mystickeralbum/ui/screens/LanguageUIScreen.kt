package com.devgc.mystickeralbum.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devgc.mystickeralbum.LanguageRepository
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.model.LanguageOption
import com.devgc.mystickeralbum.scaffold.ui.MainScaffold
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun LanguageUIScreen() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        LanguageOption.values().forEach { option ->
            val context = LocalContext.current

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp)
                    .clickable {
                        LanguageRepository.changeLanguage(context, option.localeOption.locale)
                    }
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(vertical = 10.dp, horizontal = 10.dp)
            ) {
                Text(
                    text = stringResource(id = option.title),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1F)
                )

                if (option.localeOption.locale == LanguageRepository.getCurrentLanguage()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                    )
                }
            }


        }
    }
}

@Preview
@Composable
fun LanguageUIScreenPreview() {
    MyStickerAlbumTheme {
        MainScaffold {
            LanguageUIScreen()
        }
    }
}