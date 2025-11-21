package com.example.moduleguard.data.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant

data class ModuleUiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val response: MockResponse? = null,
    val isCoolingActive: Boolean = false,
    val coolingMessage: String? = null,
    val now: Instant = Instant.now()
)
