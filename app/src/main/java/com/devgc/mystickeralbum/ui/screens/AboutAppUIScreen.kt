package com.devgc.mystickeralbum.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.ui.components.TitleSection
import com.devgc.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun AboutAppUIScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.about_me),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.about_app_message),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.about_app_feedback),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        item {
            TitleSection(
                title = stringResource(id = R.string.about_app_contact_title),
                color = MaterialTheme.colorScheme.onTertiary
            )

            Text(
                text = stringResource(id = R.string.about_app_contact),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

            val uriHandler = LocalUriHandler.current
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_linkedin),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp)
                        .clickable {
                            uriHandler.openUri("https://www.linkedin.com/in/guilhermescastro/")
                        }
                        .padding(bottom = 20.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AboutAppUIScreenPreview() {
    MyStickerAlbumTheme {
        AboutAppUIScreen()
    }
}