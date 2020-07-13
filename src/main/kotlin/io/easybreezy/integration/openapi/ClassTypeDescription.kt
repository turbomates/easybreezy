package io.easybreezy.integration.openapi

import kotlinx.serialization.json.JsonObject

data class ClassTypeDescription(
    val description: String,
    val example: JsonObject? = null,
    val format: String? = null
)
