package io.easybreezy.infrastructure.ktor

import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.ContinuousListSerializer
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonOutput
import kotlinx.serialization.list
import kotlinx.serialization.modules.serializersModuleOf
import kotlinx.serialization.serializer
import kotlin.collections.List
import kotlin.collections.mapOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.reflect.typeOf

@Serializable(with = ResponseSerializer::class)
data class Response(
    var data: Any? = null,
    var errors: List<Error>? = null,
    var error: Error? = null,
    var list: ContinuousList<*>? = null
)

@Serializable
data class Error(val message: String, val property: String? = null, @ContextualSerialization val value: Any? = null)

suspend fun ApplicationCall.respondError(
    status: HttpStatusCode = HttpStatusCode.UnprocessableEntity,
    errors: List<Error>
) {
    this.response.status(status)
    this.respond(Response(errors = errors))
}

suspend fun ApplicationCall.respondError(
    status: HttpStatusCode = HttpStatusCode.UnprocessableEntity,
    error: Error
) {
    this.response.status(status)
    this.respond(Response(error = error))
}

suspend fun ApplicationCall.respondData(
    data: Any?,
    status: HttpStatusCode = HttpStatusCode.OK
) {
    this.response.status(status)
    this.respond(Response(data = data))
}

suspend fun ApplicationCall.respondListing(list: ContinuousList<*>, status: HttpStatusCode = HttpStatusCode.OK) {
    this.response.status(status)
    this.respond(Response(list = list))
}

suspend fun ApplicationCall.respondOk() {
    this.response.status(HttpStatusCode.OK)

    this.respond(mapOf("status" to "ok"))
}

object ResponseSerializer : KSerializer<Response> {
    override val descriptor: SerialDescriptor = SerialClassDescImpl("ContinuousListDescriptor")
    override fun serialize(encoder: Encoder, obj: Response) {
        val output = encoder as? JsonOutput ?: throw SerializationException("This class can be saved only by Json")
        val map = mutableMapOf<String, JsonElement>()
        if (obj.list != null) {
            return output.encodeJson(output.json.toJson(ContinuousListSerializer, obj.list!!))
        }
        obj.data?.let {
            map["data"] = output.json.toJson(obj.data!!::class.serializer() as KSerializer<Any>, obj.data!!)
        }
        obj.error?.let { map["error"] = output.json.toJson(obj.error!!) }
        obj.errors?.let { map["errors"] = output.json.toJson(Error.serializer().list, obj.errors!!) }
        val tree = JsonObject(map)
        output.encodeJson(tree)
    }

    override fun deserialize(decoder: Decoder): Response {
        throw NotImplementedError()
    }
}
