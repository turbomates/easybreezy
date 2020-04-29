package io.easybreezy.integration.openapi

import io.easybreezy.integration.openapi.spec.InfoObject
import io.easybreezy.integration.openapi.spec.PathItemObject
import io.easybreezy.integration.openapi.spec.ResponseObject
import io.easybreezy.integration.openapi.spec.Root
import kotlinx.serialization.json.JsonObject

data class OpenAPI(var host: String) {
    private val root: Root = Root("3.0.2", InfoObject("Api", version = "0.1.0"))
    fun addPath(
        path: String,
        method: Method,
        responses: Map<Int, List<Description>>,
        body: List<Description> = emptyList(),
        pathParams: List<Description> = emptyList()
    ) {
        val pathItemObject = PathItemObject()
        when (method) {
            Method.GET -> {
                // pathItemObject.get = OperationObject(responses.mapValues { it.value.toResponseObject() })
            }
            Method.POST -> {
            }
            Method.DELETE -> {
            }
            Method.PATCH -> {
            }
        }
    }

    private fun Map<String, Any>.toResponseObject(): ResponseObject {
        val response = ResponseObject("sd")
        return response
    }

    enum class Method {
        GET, POST, DELETE, PATCH
    }
}

data class Description(
    val name: String,
    val type: String,
    val child: List<Description> = emptyList(),
    val example: JsonObject? = null
)

sealed class Type {
    object String : Type()
    class Array(type: Type) : Type()
    class Object(properties: List<Description>) : Type()
    object Boolean : Type()
    object Number : Type()
}
