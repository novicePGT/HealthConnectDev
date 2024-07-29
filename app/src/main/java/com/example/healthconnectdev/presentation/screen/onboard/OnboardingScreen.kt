package com.example.healthconnectdev.presentation.screen.onboard

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.connect.client.HealthConnectClient
import com.example.healthconnectdev.data.HealthConnectAvailability
import com.example.healthconnectdev.data.HealthConnectManager
import com.example.healthconnectdev.ui.theme.HealthConnectDevTheme
import kotlinx.coroutines.launch

const val HEALTH_CONNECT_SETTINGS_ACTION = "androidx.health.ACTION_HEALTH_CONNECT_SETTINGS"

@Composable
fun OnboardingScreen(
    onboardingViewModel: OnboardingViewModel,
    healthConnectManager: HealthConnectManager
) {
    HealthConnectDevTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IntroStack(name = "Health Connect Dev")
                if (healthConnectManager.availability.value == HealthConnectAvailability.INSTALLED) {
                    Spacer(modifier = Modifier.height(16.dp))
                    RequestPermissionButton(healthConnectManager)
                    StartHealthConnectSettingsButton()
                    RecordButtons(onboardingViewModel)
                }
            }
        }
    }
}

@Composable
fun IntroStack(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        fontSize = 24.sp,
        modifier = modifier
    )
}

@Composable
fun RequestPermissionButton(healthConnectManager: HealthConnectManager) {
    ActionButton(
        text = "Request Permission",
        onClick = { healthConnectManager.requestPermissions() }
    )
}

@Composable
fun StartHealthConnectSettingsButton() {
    val context = LocalContext.current
    ActionButton(
        text = "Open Health Connect Settings",
        onClick = { context.startActivity(Intent(HEALTH_CONNECT_SETTINGS_ACTION)) }
    )
}

@Composable
fun RecordButtons(onboardingViewModel: OnboardingViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val buttons = listOf(
        "1. Read Record of Hearth" to { coroutineScope.launch { onboardingViewModel.readHeartRateSessionData() } },
        "2. Read Active Calories Burned Record" to { coroutineScope.launch { onboardingViewModel.readActiveCaloriesBurnedSessionData() } },
        "3. Read Basal Body Temperature Record" to { coroutineScope.launch { onboardingViewModel.readBasalBodyTemperatureSessionData() } },
        "4. Read Basl Metabolic Rate Record" to { coroutineScope.launch { onboardingViewModel.readBasalMetabolicRateSessionData() } },
        "5. Read Blood Glucose Record" to { coroutineScope.launch { onboardingViewModel.readBloodGlucoseSessionData() } },
        "6. Blood Pressure Record" to { coroutineScope.launch { onboardingViewModel.readBloodPressureSessionData() } },
        "7. Read Body Fat Percentage Record" to { coroutineScope.launch { onboardingViewModel.readBodyFatSessionData() } },
        "8. Read Body Temperature Record" to { coroutineScope.launch { onboardingViewModel.readBodyTemperatureSessionData() } },
        "9. Read Body Water Mass Record" to { coroutineScope.launch { onboardingViewModel.readBodyWaterMassSessionData() } },
        "10. Read Bone Mass Record" to { coroutineScope.launch { onboardingViewModel.readBoneMassSessionData() } },
        "11. Read Cervical Mucus Record Record" to { coroutineScope.launch { onboardingViewModel.readCervicalMucusSessionData() } },
        "12. Read Cycling Pedaling Cadence Record" to { coroutineScope.launch { onboardingViewModel.readCyclingPedalingCadenceSessionData() } },
        "13. Read Distance Record" to { coroutineScope.launch { onboardingViewModel.readDistanceSessionData() } },
        "14. Read Elevation Gain Record" to { coroutineScope.launch { onboardingViewModel.readElevationGainedSessionData() } },
        "15. Read Exercise Session Record" to { coroutineScope.launch { onboardingViewModel.readExerciseSessionData() } },
        "16. Read Floors Climbed Record" to { coroutineScope.launch { onboardingViewModel.readFloorsClimbedSessionData() } },
        "17. Read Hydration Record" to { coroutineScope.launch { onboardingViewModel.readHydrationSessionData() } },
        "18. Read heartRate Variability Millis Record" to { coroutineScope.launch { onboardingViewModel.readHeartRateVariabilityRmssdSessionData() } },
        "19. Read Height Record" to { coroutineScope.launch { onboardingViewModel.readHeightSessionData() } },
        "20. Read Lean Body Mass Record" to { coroutineScope.launch { onboardingViewModel.readLeanBodyMassSessionData() } },
        "21. Read Nutrition Record" to { coroutineScope.launch { onboardingViewModel.readNutritionSessionData() } },
        "22. Read Oxygen Saturation Record" to { coroutineScope.launch { onboardingViewModel.readOxygenSaturationSessionData() } },
        "23. Read Power Record" to { coroutineScope.launch { onboardingViewModel.readPowerSessionData() } },
        "24. Read Respiratory Rate Record" to { coroutineScope.launch { onboardingViewModel.readRespiratoryRateSessionData() } },
        "25. Read Resting HeartRate Record" to { coroutineScope.launch { onboardingViewModel.readRestingHeartRateSessionData() } },
        "26. Read Sleep Session Record" to { coroutineScope.launch { onboardingViewModel.readSleepSessionData() } },
        "27. Read Speed Record" to { coroutineScope.launch { onboardingViewModel.readSpeedSessionData() } },
        "28. Read Steps Cadence Record" to { coroutineScope.launch { onboardingViewModel.readStepsCadenceSessionData() } },
        "29. Read Weight Record" to { coroutineScope.launch { onboardingViewModel.readWeightSessionData() } },
    )

    buttons.forEach { (text, onClick) ->
        ActionButton(text = text, onClick = onClick as () -> Unit)
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    val context = LocalContext.current
    val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context = context) }
    val healthConnectManager by lazy { HealthConnectManager(context) }
    val onboardingViewModel by lazy { OnboardingViewModel(healthConnectClient = healthConnectClient, healthConnectManager = healthConnectManager) }
    OnboardingScreen(onboardingViewModel = onboardingViewModel, healthConnectManager = HealthConnectManager(context))
}