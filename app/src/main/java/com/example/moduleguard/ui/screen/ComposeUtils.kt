package com.example.moduleguard.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.moduleguard.data.models.ModuleDto


object ComposeUtils {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppHeader(title: String) {
        CenterAlignedTopAppBar(
            title = { Text("Module Access Dashboard") }
        )

    }

    @Composable
    fun CoolingBanner(message: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }

    @Composable
    fun ModulesList(
        modules: List<ModuleDto>,
        onModuleClick: (ModuleDto) -> Unit,
        isModuleDisabled: (ModuleDto) -> Boolean
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(modules) { module ->
                ModuleCard(
                    module = module,
                    isDisabled = isModuleDisabled(module),
                    onClick = { onModuleClick(module) }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }

    @Composable
    fun ModuleCard(
        module: ModuleDto,
        isDisabled: Boolean,
        onClick: () -> Unit
    ) {
        val bgColor = if (isDisabled)
            MaterialTheme.colorScheme.surfaceVariant
        else
            MaterialTheme.colorScheme.surface

        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            enabled = !isDisabled,
            colors = CardDefaults.cardColors(containerColor = bgColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Module name
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.weight(1f)
                )

                // Right side status
                val statusText = if (isDisabled) "Denied" else "Allowed"
                val statusColor = if (isDisabled) Color.Red else Color(0xFF2E7D32)

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = statusColor
                )
            }
        }
    }




}