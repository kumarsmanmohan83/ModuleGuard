package com.example.moduleguard.ui.screen

import ads_mobile_sdk.h6
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.moduleguard.viewmodel.ModuleViewModel

@Composable
fun ModuleAccessScreen(vm: ModuleViewModel) {
    val state by vm.uiState.collectAsState()

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Title
            Text("Demo: Module Access UI", style = MaterialTheme.typography.h6, modifier = Modifier.padding(16.dp))

            // Cooling banner (top)
            state.coolingMessage?.let { msg ->
                CoolingBanner(message = msg)
            }

            Spacer(Modifier.height(8.dp))

            // Dynamic list of modules
            val modules = state.response?.modules ?: emptyList()
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                items(modules) { module ->
                    ModuleCard(module = module, onClick = {
                        val (allowed, reason) = vm.canOpenModule(module)
                        if (allowed) {
                            // show toast and navigate (navigation is not required — spec: show toast)
                            Toast.makeText(LocalContext.current, "Navigating to ${module.title}", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(LocalContext.current, reason ?: "Access denied", Toast.LENGTH_SHORT).show()
                        }
                    }, isDisabled = !vm.canOpenModule(module).first)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun CoolingBanner(message: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(12.dp), backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(message, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun ModuleCard(module: ModuleDto, onClick: () -> Unit, isDisabled: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isDisabled, onClick = onClick),
        elevation = 4.dp,
        backgroundColor = if (isDisabled) MaterialTheme.colors.onSurface.copy(alpha = 0.08f) else MaterialTheme.colors.surface
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(module.title, style = MaterialTheme.typography.body1, modifier = Modifier.weight(1f))
            if (isDisabled) Text("Access Denied", color = MaterialTheme.colors.onSurface.copy(alpha=0.6f))
            else Text("Allowed", color = Color(0xFF2E7D32))
        }
    }
}
