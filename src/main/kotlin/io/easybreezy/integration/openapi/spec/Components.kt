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
    val headers: Map<String, HeaderObject>,
    val content: Map<String, MediaTypeObject>,
    val links: Map<String, LinkObject>
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
//    val schema: TestSchemaObject,
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
    val `$ref`: String?,
    val summary: String?,
    val description: String?,
    val get: OperationObject?,
    val put: OperationObject?,
    val post: OperationObject?,
    val delete: OperationObject?,
    val options: OperationObject?,
    val head: OperationObject?,
    val patch: OperationObject?,
    val trace: OperationObject?,
    val servers: OperationObject?,
    val parameters: List<ParameterObject>?
)

@Serializable
data class OperationObject(
    val tags: List<String>?,
    val summary: String?,
    val description: String?,
    val externalDocs: ExternalDocumentationObject?,
    val operationId: String?,
    val parameters: List<ParameterObject>?,
    val requestBody: RequestBodyObject?,
    val responses: ResponseObject,
    val callbacks: Map<String, CallbackObject>?,
    val deprecated: Boolean?,
    val securitySchemaObject: Map<String, List<String>>?,
    val server: ServerObject?


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