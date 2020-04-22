package io.easybreezy.infrastructure.ktor

import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.ContinuousListSerializer
import io.easybreezy.infrastructure.serialization.serializerForSending
import kotlinx.serialization.ContextualSerialization
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

@Serializable
data class Error(val message: String, val property: String? = null, @ContextualSerialization val value: Any? = null)

@Serializable(with = ResponseSerializer::class)
sealed class Response {
    class Error(val error: io.easybreezy.infrastructure.ktor.Error) : Response()

    object Ok : Response()

    class Data<T : Any>(val data: T) : Response()

    class Errors(val errors: List<io.easybreezy.infrastructure.ktor.Error>) : Response()

    class Listing<T : Any>(val list: ContinuousList<T>) : Response()

    class Either<TL : Response, TR : Response>(val data: io.easybreezy.infrastructure.structure.Either<TL, TR>) :
        Response()
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
                            serializerForSending(value.data) as KSerializer<Any>,
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
        }
        output.encodeJson(tree)
    }

    override fun deserialize(decoder: Decoder): Response {
        throw NotImplementedError()
    }
}

