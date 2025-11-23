package com.example.moduleguard.domain

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.moduleguard.data.models.ModuleDto
import com.example.moduleguard.data.models.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

class AccessManager(private val user: UserDto) {

    /**
     * Return true when current instant is strictly between start and end.
     */
    fun isCoolingActive(now: Instant = Instant.now()): Boolean {
        val start = parseToInstant(user.coolingStartTime)
        val end = parseToInstant(user.coolingEndTime)

        if (start == null || end == null) return false
        return now.isAfter(start) && now.isBefore(end)
    }


    fun coolingCountdown(now: Instant = Instant.now()): String? {
        val end = parseToInstant(user.coolingEndTime) ?: return null

        // If cooling already finished, return null (no banner)
        if (now.isAfter(end) || now == end) return null

        val remaining = Duration.between(now, end).coerceAtLeast(Duration.ZERO)

        val totalSeconds = remaining.seconds

        return "Cooling ends in ${formatTimeWithOffset(totalSeconds)}"
    }

    private fun formatTimeWithOffset(totalSeconds: Long): String {
        val totalSecondsWithOffset = totalSeconds - (5 * 3600 + 30 * 60) // Subtract 5 hours 30 minutes

        // Ensure we don't go negative
        val adjustedSeconds = totalSecondsWithOffset.coerceAtLeast(0)

        val hours = adjustedSeconds / 3600
        val minutes = (adjustedSeconds % 3600) / 60
        val seconds = adjustedSeconds % 60

        return if (hours > 0) {
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%02d:%02d".format(minutes, seconds)
        }
    }

    fun hasPermissionFor(module: ModuleDto): Boolean {
        return module.id in user.accessibleModules
    }


    private fun parseToInstant(value: String?): Instant? {
        if (value.isNullOrBlank()) return null

        // Strip common invisible characters (BOM, ZERO WIDTH NO-BREAK SPACE, etc.)
        val cleaned = value
            .replace("\uFEFF", "")   // BOM
            .replace("\u200B", "")   // ZERO WIDTH SPACE
            .trim()

        try {
            // PRIMARY: parse ISO-8601 instant like "2025-11-22T23:42:00Z"
            return Instant.parse(cleaned)
        } catch (e: DateTimeParseException) {
            // fallback: try OffsetDateTime (handles offsets like +05:30 or no 'Z' but with offset)
            try {
                return OffsetDateTime.parse(cleaned).toInstant()
            } catch (e2: Exception) {
                Log.w(TAG, "Failed to parse date string to Instant: original='$value' cleaned='$cleaned'")
                return null
            }
        }
    }

}
