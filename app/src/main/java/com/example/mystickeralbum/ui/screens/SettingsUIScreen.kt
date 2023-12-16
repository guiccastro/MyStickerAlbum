package com.example.mystickeralbum.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystickeralbum.LanguageRepository
import com.example.mystickeralbum.R
import com.example.mystickeralbum.model.SettingsOptionItem
import com.example.mystickeralbum.navigation.MainNavComponent
import com.example.mystickeralbum.scaffold.ui.MainScaffold
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun SettingsUIScreen() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsOptionItem.values().forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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

                Image(
                    painter = painterResource(id = option.icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(28.dp)
                )

                Text(
                    text = stringResource(id = option.title),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1F)
                )

                if (option == SettingsOptionItem.LanguageSettingItem) {
                    Text(
                        text = LanguageRepository.getCurrentLanguage().toLanguageTag().uppercase(),
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7F),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_short_arrow_right),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(28.dp)
                )
            }
        }
    }
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