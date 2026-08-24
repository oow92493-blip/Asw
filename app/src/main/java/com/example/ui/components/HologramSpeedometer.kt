package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.system.SystemTelemetry
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VipGold
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HologramSpeedometer(
    telemetry: SystemTelemetry,
    modifier: Modifier = Modifier
) {
    val animatedRamPercent by animateFloatAsState(
        targetValue = telemetry.ramUsagePercent.toFloat(),
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "ram_gauge_anim"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "gauge_glow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

    val rotationDegree by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gauge_rotation"
    )

    Box(
        modifier = modifier
            .size(240.dp)
            .testTag("hologram_speedometer"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2 - 16.dp.toPx()

            // Outer decorative ring with tick marks
            drawCircle(
                color = DarkBorder,
                radius = radius + 10.dp.toPx(),
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Inner dashed ring
            val numTicks = 36
            for (i in 0 until numTicks) {
                val angle = Math.toRadians((i * (360.0 / numTicks) + rotationDegree.toDouble()))
                val startRadius = radius + 4.dp.toPx()
                val endRadius = radius + 8.dp.toPx()
                val tickColor = if (i % 6 == 0) CyberCyan.copy(alpha = 0.7f) else DarkBorder
                drawLine(
                    color = tickColor,
                    start = Offset(
                        (center.x + startRadius * cos(angle)).toFloat(),
                        (center.y + startRadius * sin(angle)).toFloat()
                    ),
                    end = Offset(
                        (center.x + endRadius * cos(angle)).toFloat(),
                        (center.y + endRadius * sin(angle)).toFloat()
                    ),
                    strokeWidth = if (i % 6 == 0) 2.dp.toPx() else 1.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Background Arc (240 degrees, from 150 to 390)
            drawArc(
                color = DarkSurfaceElevated,
                startAngle = 150f,
                sweepAngle = 240f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
            )

            // Active Sweep Arc
            val sweepProgress = (animatedRamPercent / 100f) * 240f
            val arcGradient = Brush.sweepGradient(
                0.0f to NeonEmerald,
                0.4f to CyberCyan,
                0.7f to ElectricViolet,
                1.0f to CrimsonAlert,
                center = center
            )

            drawArc(
                brush = arcGradient,
                startAngle = 150f,
                sweepAngle = sweepProgress,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
            )

            // Glowing core ring
            drawCircle(
                color = CyberCyan.copy(alpha = pulseAlpha * 0.15f),
                radius = radius * 0.72f,
                center = center
            )
        }

        // Center HUD Telemetry
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Memory,
                    contentDescription = null,
                    tint = CyberCyan,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "RAM LOAD",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
            }

            Text(
                text = "${telemetry.ramUsagePercent}%",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = when {
                        telemetry.ramUsagePercent < 60 -> CyberCyan
                        telemetry.ramUsagePercent < 80 -> VipGold
                        else -> CrimsonAlert
                    }
                )
            )

            Text(
                text = "${telemetry.usedRamMb} MB / ${telemetry.totalRamMb} MB",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextSecondary
                )
            )

            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceElevated)
                    .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "FREE: ${telemetry.freeRamMb} MB",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonEmerald,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

@Composable
fun TelemetryQuickGrid(
    telemetry: SystemTelemetry,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TelemetryCard(
            modifier = Modifier.weight(1f),
            title = "LATENCY",
            value = "${telemetry.currentPingMs} ms",
            subtext = telemetry.networkType.take(12),
            icon = Icons.Default.NetworkCheck,
            accentColor = when {
                telemetry.currentPingMs < 30 -> NeonEmerald
                telemetry.currentPingMs < 60 -> CyberCyan
                else -> VipGold
            }
        )

        TelemetryCard(
            modifier = Modifier.weight(1f),
            title = "DISPLAY",
            value = "${telemetry.refreshRateHz} Hz",
            subtext = "ULTRA SMOOTH",
            icon = Icons.Default.Speed,
            accentColor = CyberCyan
        )

        TelemetryCard(
            modifier = Modifier.weight(1f),
            title = "BATTERY",
            value = "${telemetry.batteryLevel}%",
            subtext = "${telemetry.batteryTempCelsius}°C",
            icon = Icons.Default.Thermostat,
            accentColor = if (telemetry.batteryTempCelsius > 42f) CrimsonAlert else VipGold
        )
    }
}

@Composable
private fun TelemetryCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtext: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurfaceCard)
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
            )

            Text(
                text = subtext,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    color = accentColor
                )
            )
        }
    }
}
