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
@Composable
fun ModuleAccessScreen(vm: ModuleViewModel) {
    val state by vm.uiState.collectAsState()

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Title
            Text("Demo: Module Access UI", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(16.dp))

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
                            //Toast.makeText(LocalContext.current, "Navigating to ${module.title}", Toast.LENGTH_SHORT).show()
                        } else {
                            //Toast.makeText(LocalContext.current, reason ?: "Access denied", Toast.LENGTH_SHORT).show()
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
    Card(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(message, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ModuleCard(
    module: ModuleDto,
    onClick:  () -> Unit,               // ✅ normal lambda, not @Composable
    isDisabled: Boolean
) {
    val bgColor =
        if (isDisabled)
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        else
            MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,              // ✅ Card has its own onClick in M3
        enabled = !isDisabled,
        colors = CardDefaults.cardColors(
            containerColor = bgColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp     // ✅ CardElevation, not Dp directly
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = module.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            if (isDisabled) {
                Text(
                    text = "Access Denied",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "Allowed",
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
