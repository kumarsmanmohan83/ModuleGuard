package com.example.moduleguard.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.moduleguard.data.models.ModuleDto
import com.example.moduleguard.viewmodel.ModuleViewModel

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import com.example.moduleguard.ui.screen.ComposeUtils.AppHeader
import com.example.moduleguard.ui.screen.ComposeUtils.CoolingBanner
import com.example.moduleguard.ui.screen.ComposeUtils.ModulesList

@Composable
fun ModuleAccessScreen(vm: ModuleViewModel) {
    val state by vm.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = { AppHeader(title = "Module Access Dashboard") }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Cooling banner when active
            state.coolingMessage?.let { msg ->
                CoolingBanner(message = msg)
            }

            Spacer(Modifier.height(12.dp))

            ModulesList(
                modules = state.response?.modules ?: emptyList(),
                onModuleClick = { module ->
                    val (allowed, reason) = vm.canOpenModule(module)

                    Toast.makeText(
                        context,
                        if (allowed) "Navigating to ${module.title}" else reason ?: "Access denied",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                isModuleDisabled = { module -> !vm.canOpenModule(module).first }
            )
        }
    }
}


