package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.GfxPresetEntity
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
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
import com.example.ui.viewmodel.GfxConfig

@Composable
fun GfxToolScreen(
    gfxConfig: GfxConfig,
    presets: List<GfxPresetEntity>,
    onUpdateGfx: (GfxConfig) -> Unit,
    onApplyPreset: (GfxPresetEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var appliedNotification by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("gfx_tool_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "VIP GFX OPTIMIZER",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "VULKAN / OPENGL RENDER PIPELINE TUNING",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurfaceElevated)
                        .border(1.dp, VipGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "GPU TURBO 2.0",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = VipGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }

        // Quick Presets Carousel
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "VIP GFX PRESETS",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val defaultList = if (presets.isNotEmpty()) presets else listOf(
                        GfxPresetEntity("Ultra 144Hz Competitive", "1080p FHD", 144, "Smooth", "Off", "Low", true, true),
                        GfxPresetEntity("Extreme HDR Cinematic", "2K Ultra HD", 90, "HDR Ultra", "4x MSAA", "Ultra", true, true),
                        GfxPresetEntity("Balanced eSports", "1080p FHD", 120, "Balanced", "2x MSAA", "Medium", true, false),
                        GfxPresetEntity("Battery Saver", "720p HD", 60, "Smooth", "Off", "Off", false, true)
                    )
                    items(defaultList) { preset ->
                        PresetCard(
                            preset = preset,
                            onApply = {
                                onApplyPreset(preset)
                                appliedNotification = "Applied preset: ${preset.profileName}"
                            }
                        )
                    }
                }
            }
        }

        // Notification banner if applied
        appliedNotification?.let { note ->
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceHighlight)
                        .border(1.dp, NeonEmerald, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(18.dp))
                        Text(note, color = NeonEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 1. Target FPS Setting
        item {
            GfxSectionCard(
                title = "TARGET FPS UNLOCKER",
                subtitle = "Bypass device display throttle for maximum fluidity",
                icon = Icons.Default.Speed
            ) {
                val fpsOptions = listOf(60, 90, 120, 144)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    fpsOptions.forEach { fps ->
                        val isSelected = gfxConfig.targetFps == fps
                        GfxOptionButton(
                            text = "$fps FPS",
                            isSelected = isSelected,
                            accentColor = if (fps >= 120) NeonEmerald else CyberCyan,
                            modifier = Modifier.weight(1f),
                            onClick = { onUpdateGfx(gfxConfig.copy(targetFps = fps)) }
                        )
                    }
                }
            }
        }

        // 2. Resolution Setting
        item {
            GfxSectionCard(
                title = "RENDER RESOLUTION",
                subtitle = "Internal frame buffer scaling quality",
                icon = Icons.Default.Tune
            ) {
                val resOptions = listOf("720p HD", "1080p FHD", "2K Ultra HD", "1440p QHD")
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        resOptions.take(2).forEach { res ->
                            GfxOptionButton(
                                text = res,
                                isSelected = gfxConfig.resolution == res,
                                accentColor = CyberCyan,
                                modifier = Modifier.weight(1f),
                                onClick = { onUpdateGfx(gfxConfig.copy(resolution = res)) }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        resOptions.drop(2).forEach { res ->
                            GfxOptionButton(
                                text = res,
                                isSelected = gfxConfig.resolution == res,
                                accentColor = ElectricViolet,
                                modifier = Modifier.weight(1f),
                                onClick = { onUpdateGfx(gfxConfig.copy(resolution = res)) }
                            )
                        }
                    }
                }
            }
        }

        // 3. Graphics Quality & MSAA
        item {
            GfxSectionCard(
                title = "GRAPHICS & ANTI-ALIASING",
                subtitle = "Texture LOD, HDR lighting and MSAA smoothing",
                icon = Icons.Default.AutoAwesome
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("GRAPHIC PRESET", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                    val qualities = listOf("Smooth", "Balanced", "HD", "HDR Ultra")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        qualities.forEach { q ->
                            GfxOptionButton(
                                text = q,
                                isSelected = gfxConfig.graphicQuality == q,
                                accentColor = CyberCyan,
                                modifier = Modifier.weight(1f),
                                onClick = { onUpdateGfx(gfxConfig.copy(graphicQuality = q)) }
                            )
                        }
                    }

                    Text("MSAA ANTI-ALIASING", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                    val msaaOptions = listOf("Off", "2x MSAA", "4x MSAA")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        msaaOptions.forEach { m ->
                            GfxOptionButton(
                                text = m,
                                isSelected = gfxConfig.antiAliasing == m,
                                accentColor = ElectricViolet,
                                modifier = Modifier.weight(1f),
                                onClick = { onUpdateGfx(gfxConfig.copy(antiAliasing = m)) }
                            )
                        }
                    }
                }
            }
        }

        // 4. Hardware Optimization Switches
        item {
            GfxSectionCard(
                title = "HARDWARE & ACCELERATION",
                subtitle = "Direct GPU core & low-latency driver features",
                icon = Icons.Default.Memory
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    GfxSwitchRow(
                        title = "GPU Turbo 2.0 Engine",
                        desc = "Forces max GPU frequency scaling and eliminates frame drops",
                        checked = gfxConfig.gpuTurbo,
                        onCheckedChange = { onUpdateGfx(gfxConfig.copy(gpuTurbo = it)) }
                    )
                    GfxSwitchRow(
                        title = "Vulkan API Shader Cache",
                        desc = "Pre-compiles shaders to prevent micro-stutters during combat",
                        checked = gfxConfig.vulkanOptimization,
                        onCheckedChange = { onUpdateGfx(gfxConfig.copy(vulkanOptimization = it)) }
                    )
                    GfxSwitchRow(
                        title = "Zero Lag Memory Guard",
                        desc = "Locks game process priority to high realtime scheduling class",
                        checked = gfxConfig.zeroLagMode,
                        onCheckedChange = { onUpdateGfx(gfxConfig.copy(zeroLagMode = it)) }
                    )
                }
            }
        }

        // 5. Apply All Settings Button
        item {
            Button(
                onClick = {
                    appliedNotification = "All GFX configurations locked and synchronized!"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("apply_gfx_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, tint = SelectionText)
                    Text(
                        text = "APPLY & LOCK GFX PROFILE",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = SelectionText,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PresetCard(
    preset: GfxPresetEntity,
    onApply: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onApply() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = preset.profileName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = TextPrimary
                    ),
                    maxLines = 1
                )
                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = VipGold, modifier = Modifier.size(14.dp))
            }

            Text(
                text = "${preset.fps} FPS • ${preset.resolution}",
                style = MaterialTheme.typography.labelSmall.copy(color = NeonEmerald, fontWeight = FontWeight.Bold)
            )

            Text(
                text = "${preset.graphics} • ${preset.msaa}",
                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkSurfaceElevated)
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("TAP TO APPLY", color = CyberCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun GfxSectionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
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
                    Icon(imageVector = icon, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 14.sp
                        )
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            content()
        }
    }
}

@Composable
private fun GfxOptionButton(
    text: String,
    isSelected: Boolean,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) DarkSurfaceHighlight else DarkSurfaceElevated
    val borderColor = if (isSelected) accentColor else DarkBorder

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(if (isSelected) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                color = if (isSelected) accentColor else TextSecondary,
                fontSize = 11.sp
            ),
            maxLines = 1
        )
    }
}

@Composable
private fun GfxSwitchRow(
    title: String,
    desc: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceElevated)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SelectionText,
                checkedTrackColor = CyberCyan,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = DarkBorder
            )
        )
    }
}
