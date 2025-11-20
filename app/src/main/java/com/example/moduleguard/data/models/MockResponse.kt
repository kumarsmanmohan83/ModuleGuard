package com.example.moduleguard.data.models

import kotlinx.serialization.Serializable

@Serializable
data class MockResponse(
    val user: UserDto,
    val modules: List<ModuleDto>
)
