package io.easybreezy.infrastructure.ktor.openapi

import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.integration.openapi.Property
import io.easybreezy.integration.openapi.OpenApiKType
import io.easybreezy.integration.openapi.Type
import kotlinx.serialization.json.json
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.typeOf

class DescriptionBuilder(private val type: OpenApiKType) {
    fun buildResponseMap(): Map<Int, Type> {
        return when {
            type.jvmErasure.isSubclassOf(Response.Ok::class) -> mapOf(
                200 to getOkType()
            )
            type.jvmErasure.isSubclassOf(Response.Either::class) -> buildEitherResponseMap()
            type.jvmErasure.isSubclassOf(Response.Listing::class) -> mapOf(
                200 to buildType()
            )
            type.jvmErasure.isSubclassOf(Response.Error::class) -> mapOf(
                422 to Type.Object(
                    "error",
                    listOf(
                        Property(
                            "error",
                            Type.String
                        )
                    ),
                    example = json { "status" to "Wrong response" }
                )
            )
            type.jvmErasure.isSubclassOf(Response.Errors::class) -> mapOf(
                422 to buildType()
            )
            type.jvmErasure.isSubclassOf(Response.Data::class) -> mapOf(
                200 to buildType()
            )
            else -> mapOf(
                200 to buildType()
            )
        }
    }

    fun buildType(): Type.Object {
        return type.objectType(type.jvmErasure.simpleName!!)
    }

    private fun getOkType(): Type {
        return Type.Object(
            "ok",
            listOf(
                Property(
                    "status",
                    Type.String
                )
            ),
            example = json { "status" to "ok" }
        )
    }

    private fun buildEitherResponseMap(): Map<Int, Type> {
        val data = type.jvmErasure.memberProperties.first()
        val result = mutableMapOf<Int, Type>()
        data.returnType.arguments.forEach { argument ->
            var projectionType = type.getArgumentProjectionType(argument.type!!)
            when {
                projectionType.type.isSubtypeOf(typeOf<Response.Ok>()) -> {
                    result[200] = getOkType()
                }
                projectionType.type.isSubtypeOf(typeOf<Response.Errors>()) -> {
                    result[422] = projectionType.objectType("errors")
                }
                else -> {
                    result[200] = projectionType.objectType(projectionType.jvmErasure.simpleName!!)
                }
            }
        }
        return result
    }
}
