package com.example.moduleguard.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userType: String,
    val coolingStartTime: String? = null,
    val coolingEndTime: String? = null,
    val accessibleModules: List<String> = emptyList()
)
