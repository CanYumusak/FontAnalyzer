package dev.yumusak.fontanalyzer

import android.graphics.Paint.FontMetricsInt
import android.text.TextPaint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import dev.yumusak.fontanalyzer.ui.theme.FontAnalyzerTheme

@Composable
fun FontAnalzyer(fontId: Int) = with(LocalDensity.current) {
    val fontSize = 140.sp
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            FontDisplay(fontSize, fontId)
        }
    }
}

@Suppress("DEPRECATION")
@OptIn(ExperimentalTextApi::class)
@Composable
fun FontDisplay(
    fontSize: TextUnit,  // this equals to 140.sp
    font: Int, // this font is Nationale bold
) = with(LocalDensity.current) {
    val family = FontFamily(Font(font))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center,
    ) {
        val style = androidx.compose.ui.text.TextStyle(
            fontFamily = family,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            ),
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            ),
            lineHeight = 250.sp, // THIS is the difference between [2] and [4]
        )
        val fontMetrics = TextPaint().also {
            it.typeface = ResourcesCompat.getFont(LocalContext.current, font)
            it.textSize = with(LocalDensity.current) { fontSize.toPx() }
        }.fontMetrics
        Log.e(
            "SIZE CHANGED",
            // This prints "metrics: ascent: -126.0.dp, descent: 42.0.dp"
            "metrics: ascent: ${fontMetrics.ascent.toDp()}, descent: ${fontMetrics.descent.toDp()}"
        )

        val density = LocalDensity.current
        Text(
            modifier = Modifier
                .onSizeChanged {
                    with(density) {
                        // this prints "168.0.dp"
                        Log.e("SIZE CHANGED", "to ${it.height.toDp()}")
                    }
                }
                .border(Dp.Hairline, Color.Black.copy(alpha = 0.2f))
                .background(ColorTextHeight),
            style = style,
            text = DisplayedText,
            fontSize = fontSize,
            fontFamily = family,
        )
    }
}

@Preview
@Composable
fun FontAnalzyerPreview() {
    FontAnalyzerTheme {
        FontAnalzyer(R.font.nationale_demi_bold)
    }
}

const val DisplayedText = "Áylo"

val ColorTextHeight = Color.Yellow.copy(alpha = 0.2f)
val ColorTopBottom = Color.Blue
val ColorAscentDescent = Color.DarkGray

const val FontSizeStrokeWidth = 3.0f
