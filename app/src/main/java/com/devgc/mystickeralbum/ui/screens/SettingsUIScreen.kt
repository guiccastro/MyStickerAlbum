package com.devgc.mystickeralbum.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devgc.mystickeralbum.LanguageRepository
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.model.SettingsOptionItem
import com.devgc.mystickeralbum.navigation.MainNavComponent
import com.devgc.mystickeralbum.scaffold.ui.MainScaffold
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun SettingsUIScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .appBackground()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsOptionItem.values().forEach { option ->
            val shape = RoundedCornerShape(16.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, shape)
                    .clip(shape)
                    .background(Color.White.copy(alpha = 0.10F))
                    .border(1.dp, Color.White.copy(alpha = 0.16F), shape)
                    .clickable {
                        MainNavComponent.navController.apply {
                            option.screen.apply {
                                navigateToItself(
                                    navOptions = MainNavComponent.getSingleTopWithPopUpTo(
                                        routeScreen
                                    )
                                )
                            }
                        }
                    }
                    .padding(vertical = 12.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = option.icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(28.dp)
                )

                Text(
                    text = stringResource(id = option.title),
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1F)
                )

                if (option == SettingsOptionItem.LanguageSettingItem) {
                    Text(
                        text = LanguageRepository.getCurrentLanguage().toLanguageTag().uppercase(),
                        color = Color.White.copy(alpha = 0.68F),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_short_arrow_right),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(28.dp)
                )
            }
        }
    }
}

private fun Modifier.appBackground(): Modifier {
    return background(
        Brush.verticalGradient(
            listOf(
                Color(0xFF071827),
                Color(0xFF0B2D4A),
                Color(0xFF071420)
            )
        )
    )
}

@Preview(showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    MyStickerAlbumTheme {
        MainScaffold {
            SettingsUIScreen()
        }
    }
}
