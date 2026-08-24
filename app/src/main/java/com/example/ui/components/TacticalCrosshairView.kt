package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.CrosshairConfig

@Composable
fun TacticalCrosshairView(
    config: CrosshairConfig,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(config.sizeDp.dp * 2),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val crosshairColor = Color(config.colorHex).copy(alpha = config.opacity)
            val strokePx = config.strokeWidthDp.dp.toPx()
            val radius = config.sizeDp.dp.toPx()
            val gap = radius * 0.28f

            when (config.styleIndex) {
                // 0: Precision Center Dot
                0 -> {
                    drawCircle(
                        color = crosshairColor,
                        radius = strokePx * 1.8f,
                        center = center
                    )
                    drawCircle(
                        color = crosshairColor.copy(alpha = 0.3f),
                        radius = strokePx * 3.5f,
                        center = center,
                        style = Stroke(width = strokePx * 0.7f)
                    )
                }

                // 1: Classic Tactical Cross
                1 -> {
                    // Top
                    drawLine(
                        color = crosshairColor,
                        start = Offset(center.x, center.y - gap),
                        end = Offset(center.x, center.y - radius),
                        strokeWidth = strokePx,
                        cap = StrokeCap.Round
                    )
                    // Bottom
                    drawLine(
                        color = crosshairColor,
                        start = Offset(center.x, center.y + gap),
                        end = Offset(center.x, center.y + radius),
                        strokeWidth = strokePx,
                        cap = StrokeCap.Round
                    )
                    // Left
                    drawLine(
                        color = crosshairColor,
                        start = Offset(center.x - gap, center.y),
                        end = Offset(center.x - radius, center.y),
                        strokeWidth = strokePx,
                        cap = StrokeCap.Round
                    )
                    // Right
                    drawLine(
                        color = crosshairColor,
                        start = Offset(center.x + gap, center.y),
                        end = Offset(center.x + radius, center.y),
                        strokeWidth = strokePx,
                        cap = StrokeCap.Round
                    )

                    if (config.showDot) {
                        drawCircle(color = crosshairColor, radius = strokePx * 0.9f, center = center)
                    }
                }

                // 2: Apex Circle & 4-Pips
                2 -> {
                    drawCircle(
                        color = crosshairColor,
                        radius = radius * 0.65f,
                        center = center,
                        style = Stroke(width = strokePx)
                    )
                    if (config.showDot) {
                        drawCircle(color = crosshairColor, radius = strokePx * 0.8f, center = center)
                    }
                    // 4 Small outer tick lines
                    val tickLen = radius * 0.25f
                    drawLine(crosshairColor, Offset(center.x, center.y - radius * 0.75f), Offset(center.x, center.y - radius * 0.75f - tickLen), strokePx, StrokeCap.Round)
                    drawLine(crosshairColor, Offset(center.x, center.y + radius * 0.75f), Offset(center.x, center.y + radius * 0.75f + tickLen), strokePx, StrokeCap.Round)
                    drawLine(crosshairColor, Offset(center.x - radius * 0.75f, center.y), Offset(center.x - radius * 0.75f - tickLen, center.y), strokePx, StrokeCap.Round)
                    drawLine(crosshairColor, Offset(center.x + radius * 0.75f, center.y), Offset(center.x + radius * 0.75f + tickLen, center.y), strokePx, StrokeCap.Round)
                }

                // 3: Sniper Precision Cross & Reticle Range Lines
                3 -> {
                    // Full fine cross
                    drawLine(crosshairColor, Offset(center.x, center.y - radius), Offset(center.x, center.y + radius), strokePx * 0.75f)
                    drawLine(crosshairColor, Offset(center.x - radius, center.y), Offset(center.x + radius, center.y), strokePx * 0.75f)
                    // Outer ring
                    drawCircle(crosshairColor, radius = radius * 0.85f, center = center, style = Stroke(width = strokePx * 0.8f))
                    // Reticle range notches
                    val notch1 = radius * 0.35f
                    val notch2 = radius * 0.6f
                    drawLine(crosshairColor, Offset(center.x - 4.dp.toPx(), center.y + notch1), Offset(center.x + 4.dp.toPx(), center.y + notch1), strokePx * 0.6f)
                    drawLine(crosshairColor, Offset(center.x - 6.dp.toPx(), center.y + notch2), Offset(center.x + 6.dp.toPx(), center.y + notch2), strokePx * 0.6f)
                }

                // 4: Tactical Diamond HUD
                4 -> {
                    val path = Path().apply {
                        moveTo(center.x, center.y - radius * 0.8f)
                        lineTo(center.x + radius * 0.8f, center.y)
                        lineTo(center.x, center.y + radius * 0.8f)
                        lineTo(center.x - radius * 0.8f, center.y)
                        close()
                    }
                    drawPath(path, crosshairColor, style = Stroke(width = strokePx))
                    if (config.showDot) {
                        drawCircle(crosshairColor, radius = strokePx * 1.1f, center = center)
                    }
                }

                // 5: T-Pose Competitive Aim
                5 -> {
                    // Left, Right, Bottom only (no top bar for clear headshot visibility)
                    drawLine(crosshairColor, Offset(center.x - gap, center.y), Offset(center.x - radius, center.y), strokePx, StrokeCap.Square)
                    drawLine(crosshairColor, Offset(center.x + gap, center.y), Offset(center.x + radius, center.y), strokePx, StrokeCap.Square)
                    drawLine(crosshairColor, Offset(center.x, center.y + gap), Offset(center.x, center.y + radius), strokePx, StrokeCap.Square)
                    if (config.showDot) {
                        drawCircle(crosshairColor, radius = strokePx * 0.9f, center = center)
                    }
                }
            }
        }
    }
}
