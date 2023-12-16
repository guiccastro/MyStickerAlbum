package com.example.mystickeralbum.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystickeralbum.R
import com.example.mystickeralbum.ui.theme.MyStickerAlbumTheme

@Composable
fun AboutAppUIScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8F))
            .padding(vertical = 20.dp, horizontal = 20.dp),
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
                color = Color.White
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
                color = Color.White
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
                color = Color.White
            )
        }

        item {
            TitleSection(
                title = stringResource(id = R.string.about_app_contact_title)
            )

            Text(
                text = stringResource(id = R.string.about_app_contact),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                letterSpacing = 2.sp,
                color = Color.White,
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
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp)
                        .clickable {
                            uriHandler.openUri("https://www.linkedin.com/in/guilhermescastro/")
                        }
                )
            }
        }
    }
}

@Composable
fun TitleSection(title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            thickness = 1.dp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        )

        Text(
            text = "  " + title.uppercase() + "  ",
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier
                .clip(RoundedCornerShape(0.dp))
        )

        Divider(
            thickness = 1.dp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun AboutAppUIScreenPreview() {
    MyStickerAlbumTheme {
        AboutAppUIScreen()
    }
}