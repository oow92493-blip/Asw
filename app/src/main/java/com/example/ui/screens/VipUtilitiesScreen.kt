package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.BoostHistoryEntity
import com.example.system.DnsBenchmarkResult
import com.example.ui.components.TacticalCrosshairView
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceHighlight
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SelectionText
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VipGold
import com.example.ui.viewmodel.AudioConfig
import com.example.ui.viewmodel.CrosshairConfig
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VipUtilitiesScreen(
    crosshairConfig: CrosshairConfig,
    onUpdateCrosshair: (CrosshairConfig) -> Unit,
    audioConfig: AudioConfig,
    onUpdateAudio: (AudioConfig) -> Unit,
    pingHistory: List<Int>,
    dnsResults: List<DnsBenchmarkResult>,
    isBenchmarkingDns: Boolean,
    onRunDnsBenchmark: () -> Unit,
    dndEnabled: Boolean,
    onToggleDnd: () -> Unit,
    isCoolingDown: Boolean,
    coolDownMessage: String?,
    onRunCpuCooler: () -> Unit,
    boostHistory: List<BoostHistoryEntity>,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("vip_utilities_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Bar
        item {
            Column {
                Text(
                    text = "VIP GAMING UTILITIES",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "TACTICAL AIM HUD, PING STABILIZER & AUDIO VIRTUALIZER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                )
            }
        }

        // 1. Tactical Crosshair / Aim Assist
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("tactical_crosshair_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkSurfaceElevated),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CenterFocusStrong, contentDescription = null, tint = CyberCyan)
                            }
                            Column {
                                Text(
                                    text = "TACTICAL AIM CROSSHAIR",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        fontSize = 14.sp
                                    )
                                )
                                Text(
                                    text = "Custom screen reticle for FPS precision",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        Switch(
                            checked = crosshairConfig.isEnabled,
                            onCheckedChange = { onUpdateCrosshair(crosshairConfig.copy(isEnabled = it)) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SelectionText,
                                checkedTrackColor = CyberCyan,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = DarkBorder
                            )
                        )
                    }

                    // Interactive Target Range Preview
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(DarkSurfaceElevated)
                            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Background Target Grid Lines
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val center = Offset(size.width / 2, size.height / 2)
                            drawCircle(color = DarkBorder.copy(alpha = 0.5f), radius = 40.dp.toPx(), center = center, style = Stroke(1.dp.toPx()))
                            drawCircle(color = DarkBorder.copy(alpha = 0.3f), radius = 70.dp.toPx(), center = center, style = Stroke(1.dp.toPx()))
                            drawLine(DarkBorder.copy(alpha = 0.4f), Offset(0f, center.y), Offset(size.width, center.y), 1.dp.toPx())
                            drawLine(DarkBorder.copy(alpha = 0.4f), Offset(center.x, 0f), Offset(center.x, size.height), 1.dp.toPx())
                        }

                        if (crosshairConfig.isEnabled) {
                            TacticalCrosshairView(config = crosshairConfig)
                        } else {
                            Text("Crosshair is Disabled", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    // Style selector
                    Text("RETICLE STYLE", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                    val styles = listOf("Dot", "Cross", "Apex", "Sniper", "Diamond", "T-Aim")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        styles.forEachIndexed { index, name ->
                            val isSelected = crosshairConfig.styleIndex == index
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) DarkSurfaceHighlight else DarkSurfaceElevated)
                                    .border(if (isSelected) 1.dp else 0.dp, CyberCyan, RoundedCornerShape(8.dp))
                                    .clickable { onUpdateCrosshair(crosshairConfig.copy(styleIndex = index, isEnabled = true)) }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = name,
                                    color = if (isSelected) CyberCyan else TextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    // Color picker
                    Text("RETICLE COLOR", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                    val colors = listOf(
                        0xFF00F0FF to "Cyan",
                        0xFF10B981 to "Green",
                        0xFFEF4444 to "Red",
                        0xFFFFB800 to "Gold",
                        0xFFFFFFFF to "White",
                        0xFFA855F7 to "Violet"
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        colors.forEach { (colorLong, _) ->
                            val isSelected = crosshairConfig.colorHex == colorLong
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorLong))
                                    .border(if (isSelected) 2.5.dp else 1.dp, if (isSelected) Color.White else Color.Transparent, CircleShape)
                                    .clickable { onUpdateCrosshair(crosshairConfig.copy(colorHex = colorLong, isEnabled = true)) }
                            )
                        }
                    }

                    // Size Slider
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Size: ${crosshairConfig.sizeDp.toInt()} dp", color = TextSecondary, fontSize = 12.sp)
                        Slider(
                            value = crosshairConfig.sizeDp,
                            onValueChange = { onUpdateCrosshair(crosshairConfig.copy(sizeDp = it)) },
                            valueRange = 16f..42f,
                            modifier = Modifier.width(180.dp),
                            colors = SliderDefaults.colors(thumbColor = CyberCyan, activeTrackColor = CyberCyan)
                        )
                    }
                }
            }
        }

        // 2. Network Ping Stabilizer & DNS Benchmark
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkSurfaceElevated),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Wifi, contentDescription = null, tint = NeonEmerald)
                            }
                            Column {
                                Text(
                                    text = "PING STABILIZER & DNS",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        fontSize = 14.sp
                                    )
                                )
                                Text(
                                    text = "Live packet jitter & DNS speed ranking",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        Button(
                            onClick = onRunDnsBenchmark,
                            enabled = !isBenchmarkingDns,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            if (isBenchmarkingDns) {
                                CircularProgressIndicator(color = SelectionText, modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Refresh, contentDescription = null, tint = SelectionText, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("TEST DNS", color = SelectionText, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            }
                        }
                    }

                    // Live Ping Graph
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceElevated)
                            .padding(10.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            if (pingHistory.size >= 2) {
                                val maxPing = (pingHistory.maxOrNull() ?: 100).coerceAtLeast(60).toFloat()
                                val minPing = 10f
                                val range = maxPing - minPing
                                val stepX = size.width / (pingHistory.size - 1)

                                val path = Path()
                                pingHistory.forEachIndexed { i, ping ->
                                    val normalizedY = size.height - ((ping - minPing) / range * size.height).coerceIn(4f, size.height - 4f)
                                    val x = i * stepX
                                    if (i == 0) path.moveTo(x, normalizedY) else path.lineTo(x, normalizedY)
                                    drawCircle(NeonEmerald, radius = 3.dp.toPx(), center = Offset(x, normalizedY))
                                }
                                drawPath(path, NeonEmerald, style = Stroke(width = 2.dp.toPx()))
                            }
                        }

                        Text(
                            text = "CURRENT: ${pingHistory.lastOrNull() ?: 24} ms • JITTER 0.8ms",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonEmerald,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }

                    // DNS Results
                    if (dnsResults.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            dnsResults.forEach { dns ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(DarkSurfaceElevated)
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(dns.provider, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(dns.ip, color = TextMuted, fontSize = 9.sp)
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text("${dns.latencyMs} ms", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(dns.status, color = NeonEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. CPU Cooler & Thermal Optimizer
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkSurfaceElevated),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AcUnit, contentDescription = null, tint = CyberCyan)
                            }
                            Column {
                                Text(
                                    text = "CPU COOLER & THERMAL GUARD",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        fontSize = 14.sp
                                    )
                                )
                                Text(
                                    text = "Thermal throttling elimination routine",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        Button(
                            onClick = onRunCpuCooler,
                            enabled = !isCoolingDown,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
                        ) {
                            if (isCoolingDown) {
                                CircularProgressIndicator(color = SelectionText, modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                            } else {
                                Text("COOL CPU", color = SelectionText, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }

                    coolDownMessage?.let { msg ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkSurfaceHighlight)
                                .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Text(msg, color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 4. 3D Spatial Audio Virtualizer
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkSurfaceElevated),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Headphones, contentDescription = null, tint = ElectricViolet)
                            }
                            Column {
                                Text(
                                    text = "3D AUDIO & FOOTSTEP EQUALIZER",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        fontSize = 14.sp
                                    )
                                )
                                Text(
                                    text = "Enhances enemy footsteps and spatial cues",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        Switch(
                            checked = audioConfig.isEnabled,
                            onCheckedChange = { onUpdateAudio(audioConfig.copy(isEnabled = it)) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SelectionText,
                                checkedTrackColor = ElectricViolet,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = DarkBorder
                            )
                        )
                    }

                    val audioPresets = listOf("Tactical Footsteps", "Bass Boost Pro", "Spatial 360", "Voice Clarity")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        audioPresets.forEach { preset ->
                            val isSelected = audioConfig.preset == preset
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) DarkSurfaceHighlight else DarkSurfaceElevated)
                                    .border(if (isSelected) 1.dp else 0.dp, ElectricViolet, RoundedCornerShape(8.dp))
                                    .clickable { onUpdateAudio(audioConfig.copy(preset = preset, isEnabled = true)) }
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = preset,
                                    color = if (isSelected) ElectricViolet else TextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Gaming DND Shield
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkSurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.NotificationsOff, contentDescription = null, tint = VipGold)
                        }
                        Column {
                            Text(
                                text = "GAMING DND INTERRUPT SHIELD",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = "Mute popups & notifications while gaming",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp)
                            )
                        }
                    }

                    Switch(
                        checked = dndEnabled,
                        onCheckedChange = { onToggleDnd() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = SelectionText,
                            checkedTrackColor = VipGold,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = DarkBorder
                        )
                    )
                }
            }
        }

        // 6. Boost History Logs
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                            Text(
                                text = "BOOST PERFORMANCE LOGS",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 13.sp
                                )
                            )
                        }

                        if (boostHistory.isNotEmpty()) {
                            IconButton(onClick = onClearHistory, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    if (boostHistory.isEmpty()) {
                        Text(
                            text = "No booster logs yet. Tap Ultra Turbo Boost to generate telemetry records.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted, fontSize = 11.sp)
                        )
                    } else {
                        val dateFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            boostHistory.take(5).forEach { log ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(DarkSurfaceElevated)
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(log.boostMode, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            dateFormat.format(Date(log.timestamp)) + " • " + log.details.take(32) + "...",
                                            color = TextMuted,
                                            fontSize = 9.sp
                                        )
                                    }
                                    Text("+${log.ramFreedMb} MB", color = NeonEmerald, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
