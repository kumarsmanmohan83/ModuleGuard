package com.example.moduleguard.data.repository

import android.content.Context
import com.example.moduleguard.data.models.MockResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.delay

class MockRepository(private val context: Context) {
    suspend fun loadMockData(): MockResponse {
        // simulate small delay to mimic fetching
        delay(200)
        val text = context.assets.open("mock_data.json").bufferedReader().use { it.readText() }
        return Json { ignoreUnknownKeys = true }.decodeFromString(text)
    }
}
