package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BoostingDialog
import com.example.ui.components.VipHeader
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GameVaultScreen
import com.example.ui.screens.GfxToolScreen
import com.example.ui.screens.VipUtilitiesScreen
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VipGold
import com.example.ui.viewmodel.GameBoosterViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameBoosterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                GameBoosterApp(viewModel = viewModel)
            }
        }
    }
}

data class NavTabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun GameBoosterApp(
    viewModel: GameBoosterViewModel
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val telemetry by viewModel.telemetry.collectAsStateWithLifecycle()
    val savedGames by viewModel.savedGames.collectAsStateWithLifecycle()
    val installedApps by viewModel.installedApps.collectAsStateWithLifecycle()
    val isLoadingApps by viewModel.isLoadingApps.collectAsStateWithLifecycle()
    val boostState by viewModel.boostState.collectAsStateWithLifecycle()
    val selectedBoostMode by viewModel.selectedBoostMode.collectAsStateWithLifecycle()
    val gfxConfig by viewModel.gfxConfig.collectAsStateWithLifecycle()
    val savedPresets by viewModel.savedPresets.collectAsStateWithLifecycle()
    val crosshairConfig by viewModel.crosshairConfig.collectAsStateWithLifecycle()
    val audioConfig by viewModel.audioConfig.collectAsStateWithLifecycle()
    val pingHistory by viewModel.pingHistory.collectAsStateWithLifecycle()
    val dnsResults by viewModel.dnsResults.collectAsStateWithLifecycle()
    val isBenchmarkingDns by viewModel.isBenchmarkingDns.collectAsStateWithLifecycle()
    val dndEnabled by viewModel.dndEnabled.collectAsStateWithLifecycle()
    val isCoolingDown by viewModel.isCoolingDown.collectAsStateWithLifecycle()
    val coolDownMessage by viewModel.coolDownMessage.collectAsStateWithLifecycle()
    val boostHistory by viewModel.boostHistoryLogs.collectAsStateWithLifecycle()

    val navItems = listOf(
        NavTabItem("Dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard, "tab_dashboard"),
        NavTabItem("Vault", Icons.Filled.SportsEsports, Icons.Outlined.SportsEsports, "tab_vault"),
        NavTabItem("GFX Tool", Icons.Filled.Tune, Icons.Outlined.Tune, "tab_gfx"),
        NavTabItem("VIP Suite", Icons.Filled.WorkspacePremium, Icons.Outlined.WorkspacePremium, "tab_vip")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkCanvas,
        topBar = {
            VipHeader()
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .border(
                        1.dp,
                        Brush.verticalGradient(listOf(DarkBorder, Color.Transparent)),
                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    ),
                containerColor = DarkSurfaceCard,
                tonalElevation = 8.dp
            ) {
                navItems.forEachIndexed { index, item ->
                    val isSelected = currentTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setTab(index) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = if (isSelected) CyberCyan else TextMuted,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp,
                                    color = if (isSelected) CyberCyan else TextMuted
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CyberCyan,
                            selectedTextColor = CyberCyan,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                            indicatorColor = DarkSurfaceElevated
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> DashboardScreen(
                    telemetry = telemetry,
                    savedGames = savedGames,
                    selectedBoostMode = selectedBoostMode,
                    onSelectBoostMode = { viewModel.selectBoostMode(it) },
                    onTriggerBoost = { viewModel.triggerUltraBoost() },
                    onBoostAndLaunchGame = { viewModel.boostAndLaunchGame(it) },
                    onNavigateToVault = { viewModel.setTab(1) }
                )
                1 -> GameVaultScreen(
                    savedGames = savedGames,
                    installedApps = installedApps,
                    isLoadingApps = isLoadingApps,
                    onAddAppToVault = { viewModel.addGameToVault(it) },
                    onToggleFavorite = { viewModel.toggleGameFavorite(it) },
                    onRemoveGame = { viewModel.removeGame(it) },
                    onBoostAndLaunchGame = { viewModel.boostAndLaunchGame(it) },
                    onRefreshApps = { viewModel.loadInstalledApps() }
                )
                2 -> GfxToolScreen(
                    gfxConfig = gfxConfig,
                    presets = savedPresets,
                    onUpdateGfx = { viewModel.updateGfxConfig(it) },
                    onApplyPreset = { viewModel.applyPreset(it) }
                )
                3 -> VipUtilitiesScreen(
                    crosshairConfig = crosshairConfig,
                    onUpdateCrosshair = { viewModel.updateCrosshair(it) },
                    audioConfig = audioConfig,
                    onUpdateAudio = { viewModel.updateAudio(it) },
                    pingHistory = pingHistory,
                    dnsResults = dnsResults,
                    isBenchmarkingDns = isBenchmarkingDns,
                    onRunDnsBenchmark = { viewModel.runDnsBenchmark() },
                    dndEnabled = dndEnabled,
                    onToggleDnd = { viewModel.toggleDnd() },
                    isCoolingDown = isCoolingDown,
                    coolDownMessage = coolDownMessage,
                    onRunCpuCooler = { viewModel.runCpuCooler() },
                    boostHistory = boostHistory,
                    onClearHistory = { viewModel.clearHistory() }
                )
            }

            // Global Turbo Boost holographic dialog
            BoostingDialog(
                boostState = boostState,
                onDismiss = { viewModel.dismissBoostDialog() }
            )
        }
    }
}
