package com.example.moduleguard.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.moduleguard.data.models.ModuleDto
import com.example.moduleguard.data.models.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.time.ZoneOffset

class AccessManager(private val user: UserDto) {

    // is cooling active
    @RequiresApi(Build.VERSION_CODES.O)
    fun isCoolingActive(now: Instant = Instant.now()): Boolean {
        val start = parseInstant(user.coolingStartTime) ?: return false
        val end = parseInstant(user.coolingEndTime) ?: return false
        return now.isAfter(start) && now.isBefore(end)
    }

    // returns "Cooling ends in MM:SS" or null if not active
    @RequiresApi(Build.VERSION_CODES.O)
    fun coolingCountdown(now: Instant = Instant.now()): String? {
        val end = parseInstant(user.coolingEndTime) ?: return null
        if (now.isAfter(end)) return null
        val remaining = Duration.between(now, end).coerceAtLeast(Duration.ZERO)
        val mm = remaining.toMinutes()
        val ss = remaining.minusMinutes(mm).seconds
        return "Cooling ends in %02d:%02d".format(mm, ss)
    }

    fun hasPermissionFor(module: ModuleDto): Boolean {
        // if module requires consent but not in accessibleModules -> deny
        if (module.id !in user.accessibleModules) return false
        // additional rules can be added here based on userType
        return true
    }

    // helper parse
    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseInstant(s: String?): Instant? {
        return try { if (s == null) null else Instant.parse(s) } catch (e: DateTimeParseException) { null }
    }
}
