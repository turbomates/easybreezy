package io.easybreezy.infrastructure.query

import io.easybreezy.infrastructure.serialization.elementSerializer
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonOutput
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow

fun <T> Query.toContinuousList(page: PagingParameters, map: ResultRow.() -> T): ContinuousList<T> {
    this.limit(page.pageSize + 1, page.offset)
    var result = this.map { map(it) }
    var hasMore = false
    if (result.count() > page.pageSize) {
        hasMore = result.count() > page.pageSize
        result = result.dropLast(1)
    }

    return ContinuousList(result, page.pageSize, page.currentPage, hasMore)
}

class ContinuousList<T>(
    val data: List<T>,
    val pageSize: Int,
    val currentPage: Int,
    val hasMore: Boolean = false
)

object ContinuousListSerializer : KSerializer<ContinuousList<*>> {
    override val descriptor: SerialDescriptor = SerialDescriptor("ContinuousListDescriptor")

    @Suppress("UNCHECKED_CAST")
    override fun serialize(encoder: Encoder, value: ContinuousList<*>) {
        val output = encoder as? JsonOutput ?: throw SerializationException("This class can be saved only by Json")
        val tree = JsonObject(
            mapOf(
                "pageSize" to JsonPrimitive(value.pageSize),
                "currentPage" to JsonPrimitive(value.currentPage),
                "hasMore" to JsonPrimitive(value.hasMore),
                "data" to output.json.toJson(value.data.elementSerializer().list as KSerializer<Any>, value.data)
            )
        )
        output.encodeJson(tree)
    }

    override fun deserialize(decoder: Decoder): ContinuousList<*> {
        throw NotImplementedError()
    }
}


