package com.example.healthconnectdev

import android.app.Application
import com.example.healthconnectdev.data.HealthConnectManager

class BaseApplication : Application() {
    val healthConnectManager by lazy {
        HealthConnectManager(this)
    }
}