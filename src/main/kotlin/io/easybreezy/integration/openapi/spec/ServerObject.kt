package io.easybreezy.integration.openapi.spec

import kotlinx.serialization.Serializable

@Serializable
data class ServerObject(val url: String, val description: String, val variables: Map<String, ServerVariableObject>)

@Serializable
data class ServerVariableObject(val enum: String, val default: String, val description: String)
