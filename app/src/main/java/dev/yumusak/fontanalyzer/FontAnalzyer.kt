package dev.yumusak.fontanalyzer

import android.graphics.Paint
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import dev.yumusak.fontanalyzer.ui.theme.FontAnalyzerTheme
import dev.yumusak.fontanalyzer.ui.theme.Typography

@Composable
fun FontAnalzyer(fontId: Int) = with(LocalDensity.current) {
    val fontSize = 140.sp
    val typeface = ResourcesCompat.getFont(LocalContext.current, fontId)
    val paint = Paint().also {
        it.typeface = typeface
        it.textSize = fontSize.toPx()
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            FontDisplay(paint, fontSize, fontId)
            FontMetricsList(paint)
        }
    }
}

@Composable
fun FontMetricsList(
    paint: Paint
) = with(LocalDensity.current) {
    val fontMetrics = paint.fontMetrics
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Metric(ColorTopBottom, "Top", fontMetrics.top)
        Metric(ColorAscentDescent, "Ascent", fontMetrics.ascent)
        Metric(ColorAscentDescent, "Descent", fontMetrics.descent)
        Metric(ColorTopBottom, "Bottom", fontMetrics.bottom)

        val height = StaticLayout.Builder.obtain(
            "Fylo",
            0,
            4,
            TextPaint(paint),
            Integer.MAX_VALUE
        ).build().height
        Metric(null, "Font size", paint.textSize)
        Metric(ColorTextHeight, "Height", height.toFloat())
    }
}

@Composable
private fun ColumnScope.Metric(color: Color?, text: String, value: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (color != null) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color)
            )
            Spacer(modifier = Modifier.size(8.dp))
        } else {
            Spacer(modifier = Modifier.size(16.dp))
        }
        Text(text = text, style = Typography.titleLarge)
        Text(
            textAlign = TextAlign.End,
            text = String.format("%.2f", value),
            modifier = Modifier.fillMaxWidth(),
            style = Typography.titleLarge
        )
    }
}

@Composable
fun FontDisplay(
    paint: Paint,
    fontSize: TextUnit,
    font: Int
) {
    val family = FontFamily(Font(font))
    val fontMetrics = paint.fontMetrics
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .border(Dp.Hairline, Color.Black.copy(alpha = 0.2f))
                .background(ColorTextHeight)
                .drawBehind {
                    translate(top = -fontMetrics.top) {
                        // Baseline
                        FontMetrics(fontMetrics)
                    }
                },
            text = "Fylo",
            fontSize = fontSize,
            fontFamily = family,
        )
    }
}

private fun DrawScope.FontMetrics(fontMetrics: Paint.FontMetrics) {
    drawLine(
        Color.Red,
        strokeWidth = FontSizeStrokeWidth,
        start = Offset(0f, 0f),
        end = Offset(size.width, 0f)
    )

    // Baseline
    drawLine(
        Color.Red,
        strokeWidth = FontSizeStrokeWidth,
        start = Offset(0f, 0f),
        end = Offset(size.width, 0f)
    )

    // Descent
    drawLine(
        ColorAscentDescent,
        strokeWidth = FontSizeStrokeWidth,
        start = Offset(0f, fontMetrics.descent),
        end = Offset(size.width, fontMetrics.descent)
    )

    // Ascent
    drawLine(
        ColorAscentDescent,
        strokeWidth = FontSizeStrokeWidth,
        start = Offset(0f, fontMetrics.ascent),
        end = Offset(size.width, fontMetrics.ascent)
    )

    // Top
    drawLine(
        ColorTopBottom,
        strokeWidth = FontSizeStrokeWidth,
        start = Offset(0f, fontMetrics.top),
        end = Offset(size.width, fontMetrics.top)
    )

    // Bottom
    drawLine(
        ColorTopBottom,
        strokeWidth = FontSizeStrokeWidth,
        start = Offset(0f, fontMetrics.bottom),
        end = Offset(size.width, fontMetrics.bottom)
    )
}

@Preview
@Composable
fun FontAnalzyerPreview() {
    FontAnalyzerTheme {
        FontAnalzyer(R.font.nationale_demi_bold)
    }
}

val ColorTextHeight = Color.Yellow.copy(alpha = 0.2f)
val ColorTopBottom = Color.Blue
val ColorAscentDescent = Color.DarkGray

const val FontSizeStrokeWidth = 3.0f