package com.example.healthconnectdev.presentation.screen.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.health.connect.client.HealthConnectClient
import com.example.healthconnectdev.BaseApplication
import com.example.healthconnectdev.data.HealthConnectManager
import com.example.healthconnectdev.presentation.screen.onboard.OnboardingScreen
import com.example.healthconnectdev.presentation.screen.onboard.OnboardingViewModel

class MainActivity : ComponentActivity() {
    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(this) }
    private val healthConnectManager by lazy { HealthConnectManager(this) }
    private val onboardingViewModel by lazy {
        OnboardingViewModel(healthConnectManager = healthConnectManager, healthConnectClient = healthConnectClient)
    }
    private val availability = healthConnectManager.availability.value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val healthConnectManager = (application as BaseApplication).healthConnectManager
        healthConnectManager.checkAvailability()

        setContent {
            OnboardingScreen(onboardingViewModel = onboardingViewModel, healthConnectManager = healthConnectManager)
        }
    }
}