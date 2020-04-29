package io.easybreezy.infrastructure.ktor.openapi

import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.integration.openapi.Description
import kotlinx.serialization.json.json
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

inline fun <T : Any> buildOpenApiResponseMap(type: KType, clazz: KClass<T>): Map<Int, List<Description>> {
    return when {
        clazz.isSubclassOf(Response.Ok::class) -> mapOf(
            200 to listOf(
                Description(
                    "status",
                    "string",
                    example = json { "status" to "ok" })
            )
        )
        clazz.isSubclassOf(Response.Either::class) -> buildEitherResponseMap(type)
        clazz.isSubclassOf(Response.Listing::class) -> mapOf(
            200 to listOf(
                Description(
                    "data",
                    "object",
                    buildOpenApiParametersMap(type)
                )
            )
        )
        clazz.isSubclassOf(Response.Error::class) -> mapOf(
            422 to listOf(
                Description(
                    "error",
                    "string",
                    example = json { "status" to "Wrong response" })
            )
        )
        clazz.isSubclassOf(Response.Errors::class) -> mapOf(
            200 to listOf(
                Description(
                    "errors",
                    "object",
                    child = buildOpenApiParametersMap(type)
                )
            )
        )
        clazz.isSubclassOf(Response.Data::class) -> mapOf(
            200 to listOf(
                Description(
                    "data",
                    "object",
                    child = buildOpenApiParametersMap(type)
                )
            )
        )
        else -> mapOf(
            200 to listOf(
                Description(
                    "status",
                    "string",
                    example = json { "status" to "ok" })
            )
        )
    }
}

fun buildEitherResponseMap(type: KType): Map<Int, List<Description>> {
    val types = type.arguments
    types.forEach {
        val test = it.type?.jvmErasure?.java?.componentType
        it.variance
    }
    return emptyMap()
}

fun buildOpenApiParametersMap(type: KType): List<Description> {
    val descriptions = mutableListOf<Description>()
    type.jvmErasure.memberProperties.forEach {
        if (it.javaClass.isPrimitive ||
            it.returnType.isSubtypeOf(typeOf<String?>()) ||
            it.returnType.isSubtypeOf(typeOf<UUID?>())
        ) {
            descriptions.add(Description(it.name, it.returnType.jvmErasure.simpleName!!, emptyList()))
        } else {
            var openAPIType = "object"
            if (!it.returnType.isSubtypeOf(typeOf<Collection<*>>())) {
                descriptions.add(
                    Description(
                        it.name,
                        openAPIType,
                        child = buildOpenApiParametersMap(getType(type, it))
                    )
                )
            } else {
                descriptions.add(
                    Description(
                        it.name,
                        it.returnType.arguments[0].type?.jvmErasure?.simpleName?:"wrong"
                    )
                )
            }
        }
    }
    return descriptions
}

fun <T> getType(parent: KType, property: KProperty1<T, *>): KType {
    val types = mutableMapOf<String, KType>()
    parent.jvmErasure.typeParameters.forEachIndexed { index, kTypeParameter ->
        types[kTypeParameter.name] = parent.arguments[index].type!!
    }
    val test = property.returnType.arguments[0].type
    val test2 = property.returnType.jvmErasure.memberProperties
    var typeClass = property.returnType.jvmErasure
    var ktype = typeClass.starProjectedType
    if (property.parameters.count() > 0) {
        ktype = types[typeClass.typeParameters[0].name]!!
    }
    return ktype
}
