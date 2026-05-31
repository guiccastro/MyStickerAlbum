package com.devgc.mystickeralbum.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
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
import com.devgc.mystickeralbum.ui.theme.Poppins

@Composable
fun AboutAppUIScreen() {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .appBackground()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.about_me),
                style = Poppins,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
                color = Color.White.copy(alpha = 0.86F),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.about_app_message),
                style = Poppins,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
                color = Color.White.copy(alpha = 0.86F),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.about_app_feedback),
                style = Poppins,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
                color = Color.White.copy(alpha = 0.86F),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        item {
            TitleSection(
                title = stringResource(id = R.string.about_app_contact_title),
                color = Color.White
            )

            Text(
                text = stringResource(id = R.string.about_app_contact),
                style = Poppins,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
                color = Color.White.copy(alpha = 0.86F),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

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
                            uriHandler.openUri(context.resources.getString(R.string.linkedin_url))
                        }
                )
            }
        }

        item {
            TitleSection(
                title = stringResource(id = R.string.legal_title)
            )

            Text(
                text = stringResource(id = R.string.privacy_policy_desc),
                style = Poppins,
                color = Color.White.copy(alpha = 0.86F),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                letterSpacing = 0.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            Text(
                text = stringResource(id = R.string.privacy_policy_button),
                style = Poppins,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                letterSpacing = 0.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .shadow(4.dp, CircleShape)
                    .background(Color.White.copy(alpha = 0.10F), CircleShape)
                    .border(1.dp, Color.White.copy(alpha = 0.22F), CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        uriHandler.openUri(context.resources.getString(R.string.privacy_policy_url))
                    }
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            )
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
fun AboutAppUIScreenPreview() {
    MyStickerAlbumTheme {
        AboutAppUIScreen()
    }
}
