package io.easybreezy.integration.openapi.spec

import kotlinx.serialization.Serializable

@Serializable
data class LicenseObject(val name: String, val url: String)