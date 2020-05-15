package io.easybreezy.integration.openapi.ktor

import io.easybreezy.integration.openapi.OpenAPI
import io.easybreezy.integration.openapi.OpenApiKType
import io.easybreezy.integration.openapi.Type
import io.easybreezy.integration.openapi.openApiKType
import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.application.featureOrNull
import io.ktor.features.CORS
import io.ktor.http.HttpMethod
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext
import kotlin.reflect.KType

class OpenApi(configuration: Configuration) {
    private val typeBuilder: (OpenApiKType) -> Type.Object = configuration.typeBuilder
    private val responseBuilder: (OpenApiKType) -> Map<Int, Type> = configuration.responseBuilder
    private val openApi: OpenAPI = configuration.openApi
    private val path: String = configuration.path

    class Configuration {
        var typeBuilder: (OpenApiKType) -> Type.Object = { type -> type.objectType("response") }
        var responseBuilder: (OpenApiKType) -> Map<Int, Type> = { type -> mapOf(200 to type.type()) }
        var path = "/openapi.json"
        var configure: (OpenApi) -> Unit = {}
        var openApi: OpenAPI = OpenAPI("localhost")
    }

    fun addPath(
        path: String,
        method: HttpMethod,
        response: KType,
        body: KType? = null,
        pathParams: KType? = null
    ) {
        openApi.addPath(
            path,
            OpenAPI.Method.valueOf(method.value),
            responseBuilder(response.openApiKType),
            body?.openApiKType?.let(typeBuilder),
            pathParams?.openApiKType?.let(typeBuilder)
        )
    }

    suspend fun intercept(context: PipelineContext<Unit, ApplicationCall>) {
        if (context.call.request.path() == path) {
            context.call.respond(openApi.root)
            context.finish()
        }
    }

    /**
     * Installable feature for [OpenApi].
     */
    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, OpenApi> {
        override val key = AttributeKey<OpenApi>("OpenApi")
        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): OpenApi {
            val configuration = Configuration().apply(configure)
            val cors = pipeline.featureOrNull(CORS)
            val feature = OpenApi(configuration)
            configuration.configure(feature)
            pipeline.intercept(ApplicationCallPipeline.Setup) {
                cors?.intercept(this)
                feature.intercept(this)
            }
            return feature
        }
    }
}
