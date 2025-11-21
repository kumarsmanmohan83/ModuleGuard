package com.example.moduleguard.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moduleguard.data.models.MockResponse
import com.example.moduleguard.data.models.ModuleDto
import com.example.moduleguard.data.models.ModuleUiState
import com.example.moduleguard.data.repository.MockRepository
import com.example.moduleguard.domain.AccessManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

class ModuleViewModel(private val repo: MockRepository): ViewModel() {

    private val _uiState = MutableStateFlow(ModuleUiState())
    val uiState: StateFlow<ModuleUiState> = _uiState.asStateFlow()

    private var accessManager: AccessManager? = null

    fun load() {
        viewModelScope.launch {
            val res = repo.loadMockData()
            accessManager = AccessManager(res.user)
            updateUI(res)
            startTicker()
        }
    }

    private fun updateUI(response: MockResponse) {
        val now = Instant.now()
        val am = AccessManager(response.user)
        _uiState.update { it.copy(
            response = response,
            isCoolingActive = am.isCoolingActive(now),
            coolingMessage = am.coolingCountdown(now),
            now = now
        )}
    }

    // updates every second to refresh countdown
    private fun startTicker() {
        viewModelScope.launch {
            while(true) {
                _uiState.value.response?.let { resp -> updateUI(resp) }
                delay(1000)
            }
        }
    }


    fun canOpenModule(module: ModuleDto): Pair<Boolean, String?> {
        val resp = _uiState.value.response ?: return false to "Data not loaded"
        val am = AccessManager(resp.user)
        if (am.isCoolingActive()) return false to "Access denied: cooling period"
        if (!am.hasPermissionFor(module)) return false to "Access denied: no permission"
        return true to null
    }
}