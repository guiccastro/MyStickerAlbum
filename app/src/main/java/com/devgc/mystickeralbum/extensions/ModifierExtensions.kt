package com.devgc.mystickeralbum.extensions

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.innerShadow(
    color: Color = Color.Black,
    cornersRadius: Dp = 0.dp,
    spread: Dp = 0.dp,
    blur: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawWithContent {

    drawContent()

    val rect = Rect(Offset.Zero, size)
    val paint = Paint()

    drawIntoCanvas {

        paint.color = color
        paint.isAntiAlias = true
        it.saveLayer(rect, paint)
        it.drawRoundRect(
            left = rect.left,
            top = rect.top,
            right = rect.right,
            bottom = rect.bottom,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        if (blur.toPx() > 0) {
            frameworkPaint.maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
        val left = if (offsetX > 0.dp) {
            rect.left + offsetX.toPx()
        } else {
            rect.left
        }
        val top = if (offsetY > 0.dp) {
            rect.top + offsetY.toPx()
        } else {
            rect.top
        }
        val right = if (offsetX < 0.dp) {
            rect.right + offsetX.toPx()
        } else {
            rect.right
        }
        val bottom = if (offsetY < 0.dp) {
            rect.bottom + offsetY.toPx()
        } else {
            rect.bottom
        }
        paint.color = Color.Black
        it.drawRoundRect(
            left = left + spread.toPx() / 2,
            top = top + spread.toPx() / 2,
            right = right - spread.toPx() / 2,
            bottom = bottom - spread.toPx() / 2,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
        frameworkPaint.xfermode = null
        frameworkPaint.maskFilter = null
    }
}

fun Modifier.customShadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    spread: Dp = 0f.dp,
    widthOffset: Dp = 0.dp,
    heightOffset: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
) = this.drawBehind {
    this.drawIntoCanvas {
        size.height
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        val spreadPixel = spread.toPx()
        val leftPixel = (0f - spreadPixel) + offsetX.toPx() - widthOffset.toPx()
        val topPixel = (0f - spreadPixel) + offsetY.toPx() - heightOffset.toPx()
        val rightPixel = (this.size.width + spreadPixel) + widthOffset.toPx()
        val bottomPixel = (this.size.height + spreadPixel) + heightOffset.toPx()

        if (blurRadius != 0.dp) {
            frameworkPaint.maskFilter =
                (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
        }

        frameworkPaint.color = color.toArgb()
        it.drawRoundRect(
            left = leftPixel,
            top = topPixel,
            right = rightPixel,
            bottom = bottomPixel,
            radiusX = borderRadius.toPx(),
            radiusY = borderRadius.toPx(),
            paint
        )
    }
}


fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawWithContent {
            drawContent()
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.topBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawWithContent {
            drawContent()
            val width = size.width
            val height = strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.leftBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawWithContent {
            drawContent()
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = 0f, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.rightBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawWithContent {
            drawContent()
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = width, y = 0f),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)