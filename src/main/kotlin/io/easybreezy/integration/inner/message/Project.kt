package io.easybreezy.integration.inner.message

import kotlinx.serialization.Serializable

@Serializable
data class Project(val id: String, val name: String, val url: String) {
}