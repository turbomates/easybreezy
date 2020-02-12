package io.easybreezy.integration.openapi

import io.easybreezy.integration.openapi.spec.InfoObject
import io.easybreezy.integration.openapi.spec.OperationObject
import io.easybreezy.integration.openapi.spec.PathItemObject
import io.easybreezy.integration.openapi.spec.ResponseObject
import io.easybreezy.integration.openapi.spec.Root
import kotlin.reflect.KClass

data class OpenAPI(val host: String) {
    private val root: Root = Root("3.0.2", InfoObject("Api", version = "0.1.0"))
    fun addPath(
        path: String,
        method: Method,
        responses: Map<Int, KClass<*>>,
        body: KClass<*>,
        pathParams: KClass<*>
    ) {
        val pathItemObject = PathItemObject()
        when (method) {
            Method.GET -> {
                pathItemObject.get = OperationObject(responses.mapValues { it.value.toResponseObject() })
            }
            Method.POST -> {
            }
            Method.DELETE -> {
            }
            Method.PUT -> {
            }
            Method.PATCH -> {
            }
        }
    }

    private fun <T : Any> KClass<T>.toResponseObject(): ResponseObject {
        val response = ResponseObject("sd")
        return response
    }
}


enum class Method {
    GET, POST, DELETE, PUT, PATCH
}