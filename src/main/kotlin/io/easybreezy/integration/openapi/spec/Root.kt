package io.easybreezy.integration.openapi.spec

import kotlinx.serialization.Serializable

@Serializable
data class Root(
    val openapi: String,
    val info: InfoObject,
    val path: MutableMap<String, PathItemObject> = mutableMapOf(),
    val servers: ServerObject? = null,
    val components: Components? = null,
    val security: List<SecuritySchemaObject>? = null,
    val tags: List<TagObject>? = null,
    val externalDocs: ExternalDocumentationObject? = null
)

@Serializable
data class InfoObject(
    val title: String,
    val description: String? = null,
    val termsOfService: String? = null,
    val contact: ContactObject? = null,
    val license: LicenseObject? = null,
    val version: String
)

@Serializable
data class TagObject(
    val name: String,
    val description: String?,
    val externalDocs: ExternalDocumentationObject?
)
