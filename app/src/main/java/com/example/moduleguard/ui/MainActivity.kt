package com.example.moduleguard.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moduleguard.data.repository.MockRepository
import com.example.moduleguard.ui.screen.ModuleAccessScreen
import com.example.moduleguard.ui.theme.ModuleGuardTheme
import com.example.moduleguard.viewmodel.ModuleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repo = MockRepository(applicationContext)
        val vm = ModuleViewModel(repo)
        vm.load()

        setContent {
            ModuleGuardTheme {
                ModuleAccessScreen(vm)
            }
        }
    }
}
