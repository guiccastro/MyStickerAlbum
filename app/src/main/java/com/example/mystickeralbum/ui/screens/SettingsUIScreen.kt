package com.example.mystickeralbum.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystickeralbum.LanguageRepository
import com.example.mystickeralbum.extensions.bottomBorder
import com.example.mystickeralbum.extensions.topBorder
import com.example.mystickeralbum.model.SettingsOptionItem
import com.example.mystickeralbum.navigation.MainNavComponent
import com.example.mystickeralbum.scaffold.ui.MainScaffold
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun SettingsUIScreen() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5F))
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SettingsOptionItem.values().forEach { option ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .topBorder((0.5).dp, Color.Black)
                    .bottomBorder((0.5).dp, Color.Black)
                    .shadow(4.dp)
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
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = option.title),
                    color = Color.Black,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1F)
                )

                if (option == SettingsOptionItem.LanguageSettingItem) {
                    Text(
                        text = LanguageRepository.getCurrentLanguage().toLanguageTag().uppercase(),
                        color = Color.Black.copy(alpha = 0.5F),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    MyStickerAlbumTheme {
        MainScaffold {
            Surface {
                SettingsUIScreen()
            }
        }
    }
}