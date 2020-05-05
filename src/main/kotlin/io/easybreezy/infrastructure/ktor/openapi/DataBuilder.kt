package io.easybreezy.infrastructure.ktor.openapi

import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.integration.openapi.Description
import io.easybreezy.integration.openapi.Type
import kotlinx.serialization.json.json
import java.util.UUID
import javax.naming.InvalidNameException
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
                    Type.String,
                    example = json { "status" to "ok" })
            )
        )
        clazz.isSubclassOf(Response.Either::class) -> buildEitherResponseMap(type)
        clazz.isSubclassOf(Response.Listing::class) -> mapOf(
            200 to listOf(
                Description(
                    "data",
                    Type.Object(buildOpenApiParametersMap(type))
                )
            )
        )
        clazz.isSubclassOf(Response.Error::class) -> mapOf(
            422 to listOf(
                Description(
                    "error",
                    Type.String,
                    example = json { "status" to "Wrong response" })
            )
        )
        clazz.isSubclassOf(Response.Errors::class) -> mapOf(
            200 to listOf(
                Description(
                    "errors",
                    Type.Object(buildOpenApiParametersMap(type))
                )
            )
        )
        clazz.isSubclassOf(Response.Data::class) -> mapOf(
            200 to listOf(
                Description(
                    "data",
                    Type.Object(buildOpenApiParametersMap(type))
                )
            )
        )
        else -> mapOf(
            200 to listOf(
                Description(
                    "status",
                    Type.Object(buildOpenApiParametersMap(type))
                )
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
        val tet = it.returnType.arguments
        if (it.returnType.isPrimitive()) {
            descriptions.add(Description(it.name, it.openApiType))
        } else {
            if (!it.returnType.isSubtypeOf(typeOf<Collection<*>>())) {
                val test = it.returnType
                descriptions.add(
                    Description(
                        it.name,
                        Type.Object(buildOpenApiParametersMap(it.returnType))
                    )
                )
            } else {
                descriptions.add(
                    Description(
                        it.name,
                        Type.String//it.returnType.arguments[0].type?.jvmErasure?.simpleName ?: "wrong"
                    )
                )
            }
        }
    }
    return descriptions
}

private fun KType.isPrimitive(): Boolean {
    return javaClass.isPrimitive ||
        isSubtypeOf(typeOf<String?>()) ||
        isSubtypeOf(typeOf<Int?>()) ||
        isSubtypeOf(typeOf<Float?>()) ||
        isSubtypeOf(typeOf<Boolean?>()) ||
        isSubtypeOf(typeOf<UUID?>())
}

private val <T, R> KProperty1<T, R>.openApiType: Type
    get() {
        return when {
            returnType.isSubtypeOf(typeOf<String?>()) -> Type.String
            returnType.isSubtypeOf(typeOf<UUID?>()) -> Type.String
            returnType.isSubtypeOf(typeOf<Int?>()) -> Type.Number
            returnType.isSubtypeOf(typeOf<Float?>()) -> Type.Number
            returnType.isSubtypeOf(typeOf<Boolean?>()) -> Type.Boolean
            else -> throw InvalidNameException("test")
        }
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
