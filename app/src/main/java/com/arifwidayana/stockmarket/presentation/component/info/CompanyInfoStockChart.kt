package com.arifwidayana.stockmarket.presentation.component.info

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arifwidayana.stockmarket.data.network.model.IntraDayInfo
import kotlin.math.round
import kotlin.math.roundToInt

@SuppressLint("ModifierParameter")
@Composable
fun CompanyInfoStockChart(
    info: List<IntraDayInfo> = emptyList(),
    modifier: Modifier = Modifier,
    graphColor: Color = Color.Green
) {
    val spacing = 100f
    val upperValue = remember(info) {
        (info.maxOfOrNull { it.close }?.plus(1))?.roundToInt() ?: 0
    }
    val lowerValue = remember(info) {
        info.minOfOrNull { it.close }?.toInt() ?: 0
    }
    val density = LocalDensity.current
    val textPaint = remember(LocalDensity) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(modifier = modifier) {
        val spacePerHour = (size.width - spacing) / info.size
        (info.indices step 2).forEach {
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    info[it].date.hour.toString(),
                    spacing + it * spacePerHour,
                    size.height - 5,
                    textPaint
                )
            }
        }
        val priceStep = (upperValue - lowerValue) / 5f
        (0..4).forEach {
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    round(lowerValue + priceStep * it).toString(),
                    30f,
                    size.height - spacing - it * size.height / 5f,
                    textPaint
                )
            }
        }
        val strokePath = Path().apply {
            val height = size.height
            for (i in info.indices) {
                val nextInfo = info.getOrNull(i + 2) ?: info.last()
                val leftRatio = (info[i].close - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo.close - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightRatio * height).toFloat()
                if (i == 0) {
                    moveTo(x1, y2)
                }
                val lastX = (x1 + x2) / 2f
                quadraticBezierTo(x1, y1, lastX, (y1 - y2) / 2f)
            }
        }
//        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
//            .asComposePath()
//            .apply {
//                lineTo(lastX,size.height-spacing)
//                lineTo(spacing,size.height - spacing)
//                close()
//            }
        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}