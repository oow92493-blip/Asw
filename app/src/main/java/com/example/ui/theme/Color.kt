package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Elegant Dark VIP Accent & Status Colors
val CyberCyan = Color(0xFFD0BCFF)       // Elegant Lavender / Primary Accent
val CyberCyanDim = Color(0xFF9A82DB)    // Deep Lavender
val VipGold = Color(0xFFE2C366)         // Refined Champagne Gold
val VipGoldLight = Color(0xFFF3DF95)    // Soft Gold Highlight
val ElectricViolet = Color(0xFFCCC2DC)  // Muted Violet
val ElectricMagenta = Color(0xFFEFB8C8) // Soft Rose Quartz
val NeonEmerald = Color(0xFF85D6A4)     // Elegant Sage Emerald
val CrimsonAlert = Color(0xFFF2B8B5)    // Elegant Soft Coral

// Background & Surface Dark System (Elegant Dark)
val DarkCanvas = Color(0xFF0F1115)           // Elegant Dark Background
val DarkSurfaceCard = Color(0xFF1B1B1F)       // Elegant Surface / Card
val DarkSurfaceElevated = Color(0xFF24262B)   // Surface Elevated
val DarkSurfaceHighlight = Color(0xFF2D3037)  // Surface Highlight / Stroke
val DarkBorder = Color(0xFF333539)            // Elegant Slate Border
val DarkBorderGlow = Color(0x66D0BCFF)        // Subtle Lavender Glow

// Text System
val TextPrimary = Color(0xFFE2E2E6)
val TextSecondary = Color(0xFF91909A)
val TextMuted = Color(0xFF6B6A74)

// Selection System
val SelectionBg = Color(0xFFD0BCFF)
val SelectionText = Color(0xFF381E72)

// Gradients
val VipGoldGradient = Brush.horizontalGradient(
    listOf(Color(0xFFE2C366), Color(0xFFD4AF37), Color(0xFFF3DF95))
)
val CyberGlowGradient = Brush.horizontalGradient(
    listOf(Color(0xFFD0BCFF), Color(0xFF9A82DB))
)
val ReactorGradient = Brush.radialGradient(
    listOf(Color(0xFFD0BCFF), Color(0xFF9A82DB), Color(0xFF0F1115))
)
val CardBorderGradient = Brush.linearGradient(
    listOf(Color(0x80D0BCFF), Color(0x309A82DB), Color(0x20000000))
)
val GoldCardBorderGradient = Brush.linearGradient(
    listOf(Color(0xFFE2C366), Color(0x40E2C366), Color(0x10000000))
)
