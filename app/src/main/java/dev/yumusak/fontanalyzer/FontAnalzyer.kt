package dev.yumusak.fontanalyzer

import android.graphics.Paint
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
    val fontSize = 70.sp
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
            FontMetricsList(paint, fontSize, fontId)
        }
    }
}

@Composable
fun FontMetricsList(
    paint: Paint,
    fontSize: TextUnit,
    font: Int
) {
    val fontMetrics = paint.fontMetrics
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Metric(Color.Blue, "Top", fontMetrics.top)
        Metric(Color.Green, "Ascent", fontMetrics.ascent)
        Metric(Color.Green, "Descent", fontMetrics.descent)
        Metric(Color.Blue, "Bottom", fontMetrics.bottom)
    }
}

@Composable
private fun ColumnScope.Metric(color: Color, text: String, value: Float) {
    Row(modifier = Modifier.fillMaxWidth()) {
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
            .height(200.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .border(Dp.Hairline, Color.Black.copy(alpha = 0.2f))
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
        start = Offset(0f, 0f),
        end = Offset(size.width, 0f)
    )

    // Baseline
    drawLine(
        Color.Red,
        start = Offset(0f, 0f),
        end = Offset(size.width, 0f)
    )

    // Descent
    drawLine(
        Color.Green,
        start = Offset(0f, fontMetrics.descent),
        end = Offset(size.width, fontMetrics.descent)
    )

    // Ascent
    drawLine(
        Color.Green,
        start = Offset(0f, fontMetrics.ascent),
        end = Offset(size.width, fontMetrics.ascent)
    )

    // Top
    drawLine(
        Color.Blue,
        start = Offset(0f, fontMetrics.top),
        end = Offset(size.width, fontMetrics.top)
    )

    // Bottom
    drawLine(
        Color.Blue,
        start = Offset(0f, fontMetrics.bottom),
        end = Offset(size.width, fontMetrics.bottom)
    )
}

@Preview
@Composable
fun FontAnalzyerPreview() {
    FontAnalyzerTheme {
        FontAnalzyer(R.font.nationale_bold)
    }
}