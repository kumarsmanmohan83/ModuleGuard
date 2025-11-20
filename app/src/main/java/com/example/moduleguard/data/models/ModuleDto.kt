package com.example.moduleguard.data.models

import kotlinx.serialization.Serializable


@Serializable
data class ModuleDto(
    val id: String,
    val title: String,
    val requiresConsent: Boolean = false
)
