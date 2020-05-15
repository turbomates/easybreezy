package io.easybreezy.integration.openapi

import io.easybreezy.integration.openapi.spec.InfoObject
import io.easybreezy.integration.openapi.spec.MediaTypeObject
import io.easybreezy.integration.openapi.spec.OperationObject
import io.easybreezy.integration.openapi.spec.ParameterObject
import io.easybreezy.integration.openapi.spec.PathItemObject
import io.easybreezy.integration.openapi.spec.RequestBodyObject
import io.easybreezy.integration.openapi.spec.ResponseObject
import io.easybreezy.integration.openapi.spec.Root
import io.easybreezy.integration.openapi.spec.SchemaObject
import kotlinx.serialization.json.JsonObject
import kotlin.reflect.KClass

class OpenAPI(var host: String) {
    val root: Root = Root("3.0.2", InfoObject("Api", version = "0.1.0"))
    val descriptions: MutableMap<String, ClassTypeDescription> = mutableMapOf()

    fun addPath(
        path: String,
        method: Method,
        responses: Map<Int, Type>,
        body: Type.Object? = null,
        pathParams: Type.Object? = null
    ) {
        val pathItemObject = PathItemObject()
        root.paths[path] = pathItemObject
        when (method) {
            Method.GET -> {
                pathItemObject.get = OperationObject(
                    responses.mapValues { it.value.toResponseObject() },
                    parameters = pathParams?.toParameterObject()
                )
            }
            Method.POST -> {
                pathItemObject.post = OperationObject(
                    responses.mapValues { it.value.toResponseObject() },
                    requestBody = body?.toRequestBodyObject(),
                    parameters = pathParams?.toParameterObject()
                )
            }
            Method.DELETE -> {
                pathItemObject.delete = OperationObject(
                    responses.mapValues { it.value.toResponseObject() },
                    requestBody = body?.toRequestBodyObject(),
                    parameters = pathParams?.toParameterObject()
                )
            }
            Method.PATCH -> {
                pathItemObject.patch = OperationObject(
                    responses.mapValues { it.value.toResponseObject() },
                    requestBody = body?.toRequestBodyObject(),
                    parameters = pathParams?.toParameterObject()
                )
            }
        }
    }

    fun addClassTypeDescription(clazz: KClass<*>, description: ClassTypeDescription) {
        descriptions[clazz.qualifiedName!!] = description
    }

    private fun Type.toResponseObject(): ResponseObject {
        return ResponseObject(
            description?.description,
            content = mapOf("application/json" to MediaTypeObject(schema = toSchemaObject()))
        )
    }

    private fun Type.toRequestBodyObject(): RequestBodyObject {
        return RequestBodyObject(
            content = mapOf("application/json" to MediaTypeObject(schema = toSchemaObject()))
        )
    }

    private fun Type.Object.toParameterObject(): List<ParameterObject> {
        return properties.map {
            ParameterObject(it.name, schema = it.type.toSchemaObject(), `in` = "path")
        }
    }

    private fun Type.toSchemaObject(): SchemaObject {
        return when (this) {
            is Type.String -> {
                SchemaObject(type = "string")
            }
            is Type.Array -> {
                SchemaObject(type = "array", items = this.type.toSchemaObject())
            }
            is Type.Object -> {
                SchemaObject(
                    type = "object",
                    properties = this.properties.associate { it.name to it.type.toSchemaObject() },
                    example = example
                )
            }
            is Type.Boolean -> {
                SchemaObject(type = "boolean")
            }
            is Type.Number -> {
                SchemaObject(type = "number")
            }
        }
    }

    enum class Method {
        GET, POST, DELETE, PATCH
    }

    val Type.description: ClassTypeDescription?
        get() {
            if (this !is Type.Object) {
                return null
            }
            return this@OpenAPI.descriptions[this.returnType]
        }
}

data class ClassTypeDescription(val description: String, val example: JsonObject? = null)

data class Property(
    val name: String,
    val type: Type
)

sealed class Type {
    object String : Type()
    class Array(val type: Type) : Type()
    class Object(
        val name: kotlin.String,
        val properties: List<Property>,
        val example: JsonObject? = null,
        val returnType: kotlin.String? = null
    ) : Type()

    object Boolean : Type()
    object Number : Type()
}
