package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.system.SystemTelemetry
import com.example.ui.components.HologramSpeedometer
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val telemetry = SystemTelemetry(
        totalRamMb = 8192,
        usedRamMb = 3450,
        freeRamMb = 4742,
        ramUsagePercent = 42,
        totalStorageGb = 256f,
        freeStorageGb = 180f,
        storageUsagePercent = 30,
        batteryLevel = 92,
        batteryTempCelsius = 35.2f,
        batteryVoltageMv = 4200,
        isCharging = false,
        batteryHealth = "Good",
        currentPingMs = 22,
        pingStatus = "Optimal",
        networkType = "Wi-Fi 6",
        refreshRateHz = 120,
        cpuUsagePercent = 38,
        estimatedCpuTempCelsius = 38.5f
    )

    composeTestRule.setContent {
      MyApplicationTheme {
        HologramSpeedometer(telemetry = telemetry)
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
