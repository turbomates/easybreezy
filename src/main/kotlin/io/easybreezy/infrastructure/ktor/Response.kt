package io.easybreezy.infrastructure.ktor

import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.ContinuousListSerializer
import io.ktor.http.HttpStatusCode
import io.ktor.response.ApplicationSendPipeline
import io.ktor.routing.Route
import io.easybreezy.infrastructure.serialization.resolveSerializer
import io.ktor.application.call
import io.ktor.response.respondFile
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonOutput
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = ErrorSerializer::class)
data class Error(val message: String, val property: String? = null, val value: Any? = null)

@Serializable(with = ResponseSerializer::class)
sealed class Response {
    class Error(val error: io.easybreezy.infrastructure.ktor.Error) : Response()

    object Ok : Response()

    class Data<T : Any>(val data: T) : Response()

    class File(val file: java.io.File) : Response()

    class Errors(val errors: List<io.easybreezy.infrastructure.ktor.Error>) : Response()

    class Listing<T : Any>(val list: ContinuousList<T>) : Response()

    class Either<TL : Response, TR : Response>(val data: io.easybreezy.infrastructure.structure.Either<TL, TR>) :
        Response()
}

class EmptyParams()

class RouteResponseInterceptor : Interceptor() {
    override fun intercept(route: Route) {
        route.sendPipeline.intercept(ApplicationSendPipeline.Before) {
            if (it is Response.File) {
                context.response.status(it.status())
                call.respondFile(it.file)
                finish()
            }
        }
        route.sendPipeline.intercept(ApplicationSendPipeline.Transform) {
            if (it is Response) {
                context.response.status(it.status())
                proceedWith(SerializableResponse(it))
            }
        }
    }
}

@Serializable(with = SerializableResponse.Companion::class)
data class SerializableResponse(val response: Response) {
    companion object : KSerializer<SerializableResponse> {
        override val descriptor: SerialDescriptor = SerialDescriptor("SerializableResponseDescriptor")

        override fun deserialize(decoder: Decoder): SerializableResponse {
            throw NotImplementedError()
        }

        override fun serialize(encoder: Encoder, obj: SerializableResponse) {
            val output = encoder as? JsonOutput ?: throw SerializationException("This class can be saved only by Json")
            output.encodeJson(output.json.toJson(Response.serializer(), obj.response))
        }
    }
}

fun Response.status(): HttpStatusCode {
    return when (this) {
        is Response.Error -> HttpStatusCode.UnprocessableEntity
        is Response.Errors -> HttpStatusCode.UnprocessableEntity
        is Response.Either<*, *> -> this.data.fold({ it.status() }, { it.status() }) as HttpStatusCode
        else -> HttpStatusCode.OK
    }
}

object ResponseSerializer : KSerializer<Response> {
    override val descriptor: SerialDescriptor = SerialDescriptor("ResponseSerializerDescriptor")

    @Suppress("UNCHECKED_CAST")
    override fun serialize(encoder: Encoder, value: Response) {
        val output = encoder as? JsonOutput ?: throw SerializationException("This class can be saved only by Json")
        val tree: JsonElement = when (value) {
            is Response.Error -> {
                output.json.toJson("error" to value.error)
            }
            is Response.Ok -> {
                JsonObject(mapOf("status" to JsonPrimitive("ok")))
            }
            is Response.Data<*> -> {
                JsonObject(
                    mapOf(
                        "data" to output.json.toJson(
                            resolveSerializer(value.data) as KSerializer<Any>,
                            value.data
                        )
                    )
                )
            }
            is Response.Listing<*> -> {
                output.json.toJson(ContinuousListSerializer, value.list)
            }
            is Response.Errors -> {
                JsonObject(mapOf("errors" to output.json.toJson(Error.serializer().list, value.errors)))
            }
            is Response.Either<*, *> -> {
                val anon = { response: Response -> output.json.toJson(response) }
                value.data.fold(anon, anon) as JsonObject
            }
            else -> throw Exception("Response serialization: shouldn't reach here")
        }
        output.encodeJson(tree)
    }

    override fun deserialize(decoder: Decoder): Response {
        throw NotImplementedError()
    }
}

object ErrorSerializer : KSerializer<Error> {
    override val descriptor: SerialDescriptor = SerialDescriptor("ErrorSerializerDescriptor")

    @Suppress("UNCHECKED_CAST")
    override fun serialize(encoder: Encoder, value: Error) {
        val output = encoder as? JsonOutput ?: throw SerializationException("This class can be saved only by Json")
        val error: MutableMap<String, JsonElement> = mutableMapOf("message" to JsonPrimitive(value.message))
        if (value.property != null && !value.property.isBlank()) error["property"] = JsonPrimitive(value.property)
        if (value.value != null)
            error["value"] = output.json.toJson(resolveSerializer(value.value) as KSerializer<Any>, value.value)

        val tree = JsonObject(error)
        output.encodeJson(tree)
    }

    override fun deserialize(decoder: Decoder): Error {
        throw NotImplementedError()
    }
}
