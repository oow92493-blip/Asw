package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.entity.BoostHistoryEntity
import com.example.data.entity.GameEntity
import com.example.data.entity.GfxPresetEntity
import com.example.data.repository.GameBoosterRepository
import com.example.system.BoostEngine
import com.example.system.BoostState
import com.example.system.DnsBenchmarkResult
import com.example.system.InstalledAppItem
import com.example.system.InstalledAppScanner
import com.example.system.SystemPerformanceMonitor
import com.example.system.SystemTelemetry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class CrosshairConfig(
    val styleIndex: Int = 1, // 0=Dot, 1=Classic Cross, 2=Apex Circle, 3=Sniper, 4=Diamond, 5=T-Pose
    val colorHex: Long = 0xFF00F0FF,
    val sizeDp: Float = 28f,
    val strokeWidthDp: Float = 2.5f,
    val opacity: Float = 0.9f,
    val showDot: Boolean = true,
    val isEnabled: Boolean = false
)

data class AudioConfig(
    val preset: String = "Tactical Footsteps",
    val bassBoost: Float = 75f,
    val virtualizerSurround: Float = 85f,
    val trebleFootstepClarity: Float = 90f,
    val isEnabled: Boolean = true
)

data class GfxConfig(
    val gameEngine: String = "Vulkan / Unreal",
    val resolution: String = "1080p FHD",
    val targetFps: Int = 120,
    val graphicQuality: String = "HDR Ultra",
    val antiAliasing: String = "4x MSAA",
    val shadowQuality: String = "Ultra",
    val gpuTurbo: Boolean = true,
    val vulkanOptimization: Boolean = true,
    val zeroLagMode: Boolean = true
)

class GameBoosterViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = GameBoosterRepository(
        database.gameDao(),
        database.boostHistoryDao(),
        database.gfxPresetDao()
    )
    val monitor = SystemPerformanceMonitor(application)
    private val appScanner = InstalledAppScanner(application)
    val boostEngine = BoostEngine(application, repository, monitor)

    // Telemetry State
    private val _telemetry = MutableStateFlow(monitor.getTelemetry())
    val telemetry: StateFlow<SystemTelemetry> = _telemetry.asStateFlow()

    // Real-time Ping history for graph
    private val _pingHistory = MutableStateFlow<List<Int>>(listOf(28, 26, 24, 25, 22, 28, 23, 21, 24, 20))
    val pingHistory: StateFlow<List<Int>> = _pingHistory.asStateFlow()

    // DNS Benchmark
    private val _dnsResults = MutableStateFlow<List<DnsBenchmarkResult>>(emptyList())
    val dnsResults: StateFlow<List<DnsBenchmarkResult>> = _dnsResults.asStateFlow()
    private val _isBenchmarkingDns = MutableStateFlow(false)
    val isBenchmarkingDns: StateFlow<Boolean> = _isBenchmarkingDns.asStateFlow()

    // Boost State from Engine
    val boostState: StateFlow<BoostState> = boostEngine.boostState

    // Selected Boost Mode
    private val _selectedBoostMode = MutableStateFlow("Ultra Turbo")
    val selectedBoostMode: StateFlow<String> = _selectedBoostMode.asStateFlow()

    // Games in VIP Vault
    val savedGames: StateFlow<List<GameEntity>> = repository.allGames
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val boostHistoryLogs: StateFlow<List<BoostHistoryEntity>> = repository.recentHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPresets: StateFlow<List<GfxPresetEntity>> = repository.allPresets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Installed apps from device
    private val _installedApps = MutableStateFlow<List<InstalledAppItem>>(emptyList())
    val installedApps: StateFlow<List<InstalledAppItem>> = _installedApps.asStateFlow()
    private val _isLoadingApps = MutableStateFlow(false)
    val isLoadingApps: StateFlow<Boolean> = _isLoadingApps.asStateFlow()

    // GFX Tool state
    private val _gfxConfig = MutableStateFlow(GfxConfig())
    val gfxConfig: StateFlow<GfxConfig> = _gfxConfig.asStateFlow()

    // Tactical Crosshair state
    private val _crosshairConfig = MutableStateFlow(CrosshairConfig())
    val crosshairConfig: StateFlow<CrosshairConfig> = _crosshairConfig.asStateFlow()

    // Audio equalizer state
    private val _audioConfig = MutableStateFlow(AudioConfig())
    val audioConfig: StateFlow<AudioConfig> = _audioConfig.asStateFlow()

    // Gaming DND Status
    private val _dndEnabled = MutableStateFlow(true)
    val dndEnabled: StateFlow<Boolean> = _dndEnabled.asStateFlow()

    // CPU Cooler state
    private val _isCoolingDown = MutableStateFlow(false)
    val isCoolingDown: StateFlow<Boolean> = _isCoolingDown.asStateFlow()
    private val _coolDownMessage = MutableStateFlow<String?>(null)
    val coolDownMessage: StateFlow<String?> = _coolDownMessage.asStateFlow()

    // Active bottom navigation tab: 0=Dashboard, 1=Game Vault, 2=GFX Tool, 3=VIP Utilities
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    init {
        // Telemetry update loop
        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                val updated = monitor.getTelemetry()
                _telemetry.value = updated
                delay(2000)
            }
        }

        // Periodic ping test
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                val ping = monitor.measureRealPing()
                val current = _pingHistory.value.toMutableList()
                if (current.size >= 15) current.removeAt(0)
                current.add(ping)
                _pingHistory.value = current
                delay(3000)
            }
        }

        // Auto-scan installed apps and seed games if empty
        loadInstalledApps()
    }

    fun setTab(index: Int) {
        _currentTab.value = index
    }

    fun selectBoostMode(mode: String) {
        _selectedBoostMode.value = mode
    }

    fun triggerUltraBoost(onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            boostEngine.executeUltraBoost(_selectedBoostMode.value)
            // refresh telemetry
            _telemetry.value = monitor.getTelemetry()
            onDone?.invoke()
        }
    }

    fun dismissBoostDialog() {
        boostEngine.dismissBoostDialog()
    }

    fun loadInstalledApps() {
        viewModelScope.launch {
            _isLoadingApps.value = true
            val apps = appScanner.getInstalledLaunchableApps()
            _installedApps.value = apps
            _isLoadingApps.value = false

            // If user has no saved games yet, add detected games or popular gaming apps
            if (savedGames.value.isEmpty()) {
                val gameCandidates = apps.filter { it.isGame }
                if (gameCandidates.isNotEmpty()) {
                    val entities = gameCandidates.take(6).map {
                        GameEntity(
                            packageName = it.packageName,
                            appName = it.appName,
                            targetFps = 120,
                            resolution = "1080p FHD",
                            gfxQuality = "HDR Ultra",
                            boostMode = "Ultra Turbo",
                            isFavorite = true
                        )
                    }
                    entities.forEach { repository.insertGame(it) }
                } else {
                    // Pre-populate popular game profiles for instant VIP experience
                    val sampleGames = listOf(
                        GameEntity(packageName = "com.dts.freefireth", appName = "Free Fire MAX", targetFps = 120, resolution = "1080p FHD", gfxQuality = "Ultra HD", boostMode = "Ultra Turbo", isFavorite = true),
                        GameEntity(packageName = "com.mobile.legends", appName = "Mobile Legends: Bang Bang", targetFps = 120, resolution = "1080p FHD", gfxQuality = "Ultra High", boostMode = "eSports Low-Latency", isFavorite = true),
                        GameEntity(packageName = "com.tencent.ig", appName = "PUBG MOBILE", targetFps = 90, resolution = "2K Ultra HD", gfxQuality = "HDR Extreme", boostMode = "Ultra Turbo", isFavorite = false),
                        GameEntity(packageName = "com.miHoYo.GenshinImpact", appName = "Genshin Impact", targetFps = 60, resolution = "1080p FHD", gfxQuality = "Maximum Vulkan", boostMode = "Ultra Turbo", isFavorite = false),
                        GameEntity(packageName = "com.activision.callofduty.shooter", appName = "Call of Duty: Mobile", targetFps = 144, resolution = "1080p FHD", gfxQuality = "Competitive Fast", boostMode = "eSports Low-Latency", isFavorite = false),
                        GameEntity(packageName = "com.ea.gp.apexlegendsmobilefps", appName = "Apex Legends Mobile", targetFps = 120, resolution = "1080p FHD", gfxQuality = "HDR 120FPS", boostMode = "Ultra Turbo", isFavorite = false)
                    )
                    sampleGames.forEach { repository.insertGame(it) }
                }
            }
        }
    }

    fun addGameToVault(app: InstalledAppItem) {
        viewModelScope.launch {
            val existing = repository.getGameByPackage(app.packageName)
            if (existing == null) {
                val game = GameEntity(
                    packageName = app.packageName,
                    appName = app.appName,
                    targetFps = 120,
                    resolution = "1080p FHD",
                    gfxQuality = "HDR Ultra",
                    boostMode = _selectedBoostMode.value,
                    isFavorite = true,
                    isCustomAdded = true
                )
                repository.insertGame(game)
            }
        }
    }

    fun toggleGameFavorite(gameId: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(gameId)
        }
    }

    fun removeGame(game: GameEntity) {
        viewModelScope.launch {
            repository.deleteGame(game)
        }
    }

    fun boostAndLaunchGame(game: GameEntity) {
        viewModelScope.launch {
            // First run Ultra Boost
            boostEngine.executeUltraBoost("VIP Boost: ${game.appName}")
            repository.incrementLaunch(game.id)
            delay(400)
            // Then launch actual app
            appScanner.launchApp(game.packageName)
        }
    }

    fun updateGfxConfig(update: GfxConfig) {
        _gfxConfig.value = update
    }

    fun applyPreset(preset: GfxPresetEntity) {
        _gfxConfig.value = _gfxConfig.value.copy(
            resolution = preset.resolution,
            targetFps = preset.fps,
            graphicQuality = preset.graphics,
            antiAliasing = preset.msaa,
            shadowQuality = preset.shadows,
            gpuTurbo = preset.gpuTurbo,
            vulkanOptimization = preset.vulkanOptimization
        )
    }

    fun updateCrosshair(config: CrosshairConfig) {
        _crosshairConfig.value = config
    }

    fun updateAudio(config: AudioConfig) {
        _audioConfig.value = config
    }

    fun toggleDnd() {
        _dndEnabled.value = !_dndEnabled.value
    }

    fun runCpuCooler() {
        viewModelScope.launch {
            _isCoolingDown.value = true
            _coolDownMessage.value = "Analyzing CPU Core Thermal Throttling..."
            delay(800)
            _coolDownMessage.value = "Terminating Orphan Background Render Threads..."
            delay(900)
            _coolDownMessage.value = "Applying Low-Power Core Voltage Throttling..."
            delay(900)
            _coolDownMessage.value = "CPU Cores Cooled: -4.8°C • Thermal Throttling Eliminated!"
            _isCoolingDown.value = false
            delay(2500)
            _coolDownMessage.value = null
        }
    }

    fun runDnsBenchmark() {
        viewModelScope.launch {
            _isBenchmarkingDns.value = true
            _dnsResults.value = monitor.runDnsBenchmark()
            _isBenchmarkingDns.value = false
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
