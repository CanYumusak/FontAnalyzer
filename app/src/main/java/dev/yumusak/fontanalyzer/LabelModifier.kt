package dev.yumusak.fontanalyzer

import android.content.Context
import android.graphics.Paint
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt


public class LabelLayoutModifier(
    val context: Context,
    val lineHeight: TextUnit,
    val fontMetrics: Paint.FontMetrics,
) : LayoutModifier {

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        val lineCount = lineCount(placeable)
        val fullHeight = (lineHeight.toPx() * lineCount).roundToInt()
        val centerOffset = floor(
            (lineHeight.toPx()
                .toDp() - fontMetrics.descent.toDp() + fontMetrics.ascent.toDp()).value / 2f
        ).dp.toPx().toInt()
        val figmaOffset = fontMetrics.ascent - fontMetrics.top
        return layout(width = placeable.width, height = fullHeight) {
            // Alignment lines are recorded with the parents automatically.
            placeable.placeRelative(
                x = 0,
                y = (centerOffset - figmaOffset).toInt()
            )
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ): Int {
        return ceilToLineHeight(measurable.maxIntrinsicHeight(width))
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ): Int {
        return ceilToLineHeight(measurable.minIntrinsicHeight(width))
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ): Int {
        return measurable.minIntrinsicWidth(height)
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ): Int {
        return measurable.maxIntrinsicWidth(height)
    }

    private fun Density.lineCount(placeable: Placeable): Int {
        val firstToLast = (placeable[LastBaseline] - placeable[FirstBaseline]).toFloat()
        return (firstToLast / lineHeight.toPx()).roundToInt() + 1
    }

    private fun Density.ceilToLineHeight(value: Int): Int {
        val lineHeightPx = lineHeight.toPx()
        return (ceil(value.toFloat() / lineHeightPx) * lineHeightPx).roundToInt()
    }
}
