package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packageName: String,
    val appName: String,
    val targetFps: Int = 120,
    val resolution: String = "1080p FHD",
    val gfxQuality: String = "HDR Ultra",
    val boostMode: String = "Ultra Turbo",
    val isFavorite: Boolean = false,
    val launchCount: Int = 0,
    val lastPlayedTimestamp: Long = 0L,
    val isCustomAdded: Boolean = false
)

@Entity(tableName = "boost_history")
data class BoostHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val boostMode: String,
    val ramFreedMb: Int,
    val pingBeforeMs: Int,
    val pingAfterMs: Int,
    val details: String
)

@Entity(tableName = "gfx_presets")
data class GfxPresetEntity(
    @PrimaryKey
    val profileName: String,
    val resolution: String,
    val fps: Int,
    val graphics: String,
    val msaa: String,
    val shadows: String,
    val gpuTurbo: Boolean,
    val vulkanOptimization: Boolean
)
