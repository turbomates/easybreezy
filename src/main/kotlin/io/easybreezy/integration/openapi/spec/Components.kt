package io.easybreezy.integration.openapi.spec

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable

@Serializable
data class Components(
    val schemas: Map<String, SchemaObject>,
    val responses: Map<String, ResponseObject>,
    val parameters: Map<String, ParameterObject>,
    val examples: Map<String, ExampleObject>,
    val requestBodies: Map<String, RequestBodyObject>,
    val headers: Map<String, HeaderObject>,
    val securitySchemes: Map<String, SecuritySchemaObject>,
    val links: Map<String, LinkObject>,
    val callbacks: Map<String, CallbackObject>
)

@Serializable
data class SchemaObject(
    val nullable: Boolean,
    val discriminator: DiscriminatorObject,
    val readOnly: Boolean,
    val writeOnly: Boolean,
//    val xml: XMLObject,
    val externalDocs: ExternalDocumentationObject,
    @ContextualSerialization val example: Any,
    val deprecated: Boolean
)

@Serializable
data class ResponseObject(
    val description: String,
    val headers: Map<String, HeaderObject>? = null,
    val content: Map<String, MediaTypeObject>? = null,
    val links: Map<String, LinkObject>? = null
)

@Serializable
data class ParameterObject(
    val name: String,
    val `in`: String,
    val description: String,
    val required: Boolean,
    val deprecated: Boolean,
    val allowEmptyValue: Boolean,
    val style: String?,
    val explode: Boolean,
    val allowReserved: Boolean,
    @ContextualSerialization val example: Any?,
    val examples: Map<String, ExampleObject>
)

@Serializable
data class ExampleObject(
    val summary: String?,
    val description: String?,
    @ContextualSerialization val value: Any?,
    val externalValue: String?
)

@Serializable
data class RequestBodyObject(val description: String, val content: Map<String, MediaTypeObject>, val required: Boolean)

@Serializable
data class DiscriminatorObject(val propertyName: String, val mapping: Map<String, String>)

@Serializable
data class MediaTypeObject(
    val schema: SchemaObject,
    @ContextualSerialization val example: Any?,
    val examples: Map<String, ExampleObject>,
    val encoding: Map<String, EncodingObject>
)

typealias HeaderObject = ParameterObject

@Serializable
data class SecuritySchemaObject(
    val type: String,
    val description: String?,
    val name: String,
    val `in`: String,
    val scheme: String,
    val bearerFormat: String?,
    val flows: OAuthFlowsObject,
    val openIdConnectUrl: String
)

@Serializable
data class OAuthFlowsObject(
    val authorizationUrl: String,
    val tokenUrl: String,
    val refreshUrl: String,
    val scopes: Map<String, String>
)

@Serializable
data class LinkObject(
    val operationRef: String?,
    val operationId: String?,
    val parameters: Map<String, @ContextualSerialization Any>?,
    @ContextualSerialization val requestBody: Any?,
    val description: String?,
    val server: ServerObject?
)

@Serializable
data class CallbackObject(val pathObject: PathsObject)

@Serializable
data class PathsObject(val path: PathItemObject)

@Serializable
data class PathItemObject(
    var `$ref`: String? = null,
    var summary: String? = null,
    var description: String? = null,
    var get: OperationObject? = null,
    var post: OperationObject? = null,
    var delete: OperationObject? = null,
    var options: OperationObject? = null,
    var head: OperationObject? = null,
    var patch: OperationObject? = null,
    var trace: OperationObject? = null,
    var servers: OperationObject? = null,
    var parameters: List<ParameterObject>? = null
)

@Serializable
data class OperationObject(
    val responses: Map<Int, ResponseObject>,
    val tags: List<String>? = null,
    val summary: String? = null,
    val description: String? = null,
    val externalDocs: ExternalDocumentationObject? = null,
    val operationId: String? = null,
    val parameters: List<ParameterObject>? = null,
    val requestBody: RequestBodyObject? = null,
    val callbacks: Map<String, CallbackObject>? = null,
    val deprecated: Boolean? = null,
    val securitySchemaObject: Map<String, List<String>>? = null,
    val server: ServerObject? = null

)

@Serializable
data class ExternalDocumentationObject(val description: String?, val url: String)

@Serializable
data class EncodingObject(
    val contentType: String?,
    val headers: Map<String, HeaderObject>?,
    val style: String?,
    val explode: Boolean?,
    val allowReserved: Boolean?
)
