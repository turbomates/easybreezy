package io.easybreezy.infrastructure.ktor

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.easybreezy.infrastructure.query.ContinuousList
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText

data class Response(
    var data: Any? = null,
    var error: Any? = null,
    var errors: Any? = null,
    var hasMore: Boolean? = null,
    var pageSize: Int? = null,
    var currentPage: Int? = null
)

suspend fun ApplicationCall.respondWith(
    status: HttpStatusCode = HttpStatusCode.OK,
    respConfigure: Response.() -> Unit
) {
    val response = Response().apply(respConfigure)

    val jsonObject = mapOf(
        "data" to response.data,
        "error" to response.error,
        "errors" to response.errors,
        "hasMore" to response.hasMore,
        "pageSize" to response.pageSize,
        "currentPage" to response.currentPage
    ).filterValues { it != null }

    this.response.status(status)
    this.respondText(toJson(jsonObject), ContentType.Application.Json)
}

suspend fun ApplicationCall.respondListing(list: ContinuousList<*>, status: HttpStatusCode = HttpStatusCode.OK) {
    val jsonObject = mapOf(
        "data" to list,
        "hasMore" to list.hasMore,
        "pageSize" to list.pageSize,
        "currentPage" to list.currentPage
    )

    this.response.status(status)
    this.respondText(toJson(jsonObject), ContentType.Application.Json)
}

private fun toJson(data: Map<String, *>): String {
    return GsonBuilder()
        .setPrettyPrinting() // TODO: Must be configurable and disabled for production
        .serializeNulls()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") // UTC Format
        .create()
        .toJson(data)
}

suspend fun ApplicationCall.respondOk() {
    this.response.status(HttpStatusCode.OK)

    this.respondText(Gson().toJson(mapOf("status" to "ok")), contentType = ContentType.Application.Json)
}
