package io.easybreezy.infrastructure.ktor

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.auth.Principal
import io.easybreezy.infrastructure.ktor.openapi.buildOpenApiParametersMap
import io.easybreezy.infrastructure.ktor.openapi.buildOpenApiResponseMap
import io.easybreezy.integration.openapi.OpenAPI
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.principal
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.locations
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.routing.application
import io.ktor.routing.method
import io.ktor.routing.route
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonOutput
import java.util.UUID
import kotlin.reflect.typeOf

open class Router @Inject constructor(
    protected val application: Application,
    protected val genericPipeline: GenericPipeline
) {

    protected inline fun <reified TController : Controller> controller(context: PipelineContext<*, ApplicationCall>): TController {
        return genericPipeline.controller(TController::class, context)
    }

    protected inline fun <reified TInterceptor : Interceptor> intercept(route: Route) {
        return genericPipeline.interceptor(TInterceptor::class).intercept(route)
    }

    protected inline fun <reified T : Principal> PipelineContext<*, ApplicationCall>.resolvePrincipal(id: UUID? = null) =
        id ?: call.principal<T>()?.id as UUID
}

inline fun <reified TResponse : Response> Route.post(
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> TResponse
): Route {
    return method(HttpMethod.Post) {
        handle {
            call.respond(body())
        }
    }
}

inline fun <reified TResponse : Response> Route.post(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> TResponse
): Route {
    openApi.addPath(
        path,
        OpenAPI.Method.valueOf(HttpMethod.Post.value),
        buildOpenApiResponseMap(typeOf<TResponse>(), TResponse::class)
    )
    return route(path, HttpMethod.Post) {
        handle {
            call.respond(body())
        }
    }
}

inline fun <reified TResponse : Response, reified TBody : Any> Route.post(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TBody) -> TResponse
): Route {
    openApi.addPath(
        path,
        OpenAPI.Method.valueOf(HttpMethod.Post.value),
        buildOpenApiResponseMap(typeOf<TResponse>(), TResponse::class),
        buildOpenApiParametersMap(typeOf<TBody>())
    )
    return route(path, HttpMethod.Post) {
        handle {
            call.respond(body(call.receive()))
        }
    }
}

inline fun <reified TResponse : Response, reified TParams : Any> Route.postParams(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TParams) -> TResponse
): Route {
    openApi.addPath(
        path,
        OpenAPI.Method.valueOf(HttpMethod.Post.value),
        buildOpenApiResponseMap(typeOf<TResponse>(), TResponse::class),
        pathParams = buildOpenApiParametersMap(typeOf<TParams>())
    )
    return route(path, HttpMethod.Post) {
        handle {
            call.respond(body(locations.resolve(TParams::class, call)))
        }
    }
}

inline fun <reified TResponse : Response, reified TBody : Any, reified TParams : Any> Route.post(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TBody, TParams) -> TResponse
): Route {
    openApi.addPath(
        path,
        OpenAPI.Method.valueOf(HttpMethod.Post.value),
        buildOpenApiResponseMap(typeOf<TResponse>(), TResponse::class),
        buildOpenApiParametersMap(typeOf<TBody>()),
        buildOpenApiParametersMap(typeOf<TParams>())
    )
    return route(path, HttpMethod.Post) {
        handle {
            call.respond(body(call.receive(), locations.resolve(TParams::class, call)))
        }
    }
}

inline fun <reified TResponse : Response, reified TBody : Any, reified TQuery : Any, reified TPath : Any> Route.post(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TBody, TPath, TQuery) -> TResponse
): Route {
    openApi.addPath(
        path,
        OpenAPI.Method.valueOf(HttpMethod.Post.value),
        buildOpenApiResponseMap(typeOf<TResponse>(), TResponse::class),
        buildOpenApiParametersMap(typeOf<TBody>()),
        buildOpenApiParametersMap(typeOf<TQuery>())
    )
    return route(path, HttpMethod.Post) {
        handle {
            call.respond(
                body(
                    call.receive(),
                    locations.resolve(TPath::class, call),
                    locations.resolve(TQuery::class, call)
                )
            )
        }
    }
}

inline fun <reified TResponse : Response> Route.get(
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> TResponse
): Route {
    return method(HttpMethod.Get) {
        handle {
            call.respond(body())
        }
    }
}

inline fun <reified TResponse : Response> Route.get(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> TResponse
): Route {
    openApi.addPath(
        path,
        OpenAPI.Method.valueOf(HttpMethod.Post.value),
        buildOpenApiResponseMap(typeOf<TResponse>(), TResponse::class)
    )
    return route(path, HttpMethod.Get) {
        handle {
            call.respond(body())
        }
    }
}

inline fun <reified TResponse : Response, reified TParams : Any> Route.get(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TParams) -> TResponse
): Route {
    openApi.addPath(
        path,
        OpenAPI.Method.valueOf(HttpMethod.Post.value),
        buildOpenApiResponseMap(typeOf<TResponse>(), TResponse::class),
        buildOpenApiParametersMap(typeOf<TParams>())
    )
    return route(path, HttpMethod.Get) {
        handle {
            call.respond(body(locations.resolve(TParams::class, call)))
        }
    }
}

inline fun <reified TResponse : Response, reified TQuery : Any, reified TPath : Any> Route.get(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TPath, TQuery) -> TResponse
): Route {
    openApi.addPath(
        path,
        OpenAPI.Method.valueOf(HttpMethod.Post.value),
        buildOpenApiResponseMap(typeOf<TResponse>(), TResponse::class),
        buildOpenApiParametersMap(typeOf<TQuery>())
    )
    return route(path, HttpMethod.Get) {
        handle {
            call.respond(body(locations.resolve(TPath::class, call), locations.resolve(TQuery::class, call)))
        }
    }
}

inline fun <reified TResponse : Response> Route.delete(
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> TResponse
): Route {
    return method(HttpMethod.Delete) {
        handle {
            call.respond(body())
        }
    }
}

inline fun <reified TResponse : Response> Route.delete(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> TResponse
): Route {
    return route(path, HttpMethod.Delete) {
        handle {
            call.respond(body())
        }
    }
}

inline fun <reified TResponse : Response, reified TParams : Any> Route.delete(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TParams) -> TResponse
): Route {
    return route(path, HttpMethod.Delete) {
        handle {
            call.respond(body(locations.resolve(TParams::class, call)))
        }
    }
}

inline fun <reified TResponse : Response, reified TBody : Any> Route.deleteWithBody(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TBody) -> TResponse
): Route {
    return route(path, HttpMethod.Delete) {
        handle {
            call.respond(body(call.receive()))
        }
    }
}

inline fun <reified TResponse : Response, reified TQuery : Any, reified TPath : Any> Route.delete(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TPath, TQuery) -> TResponse
): Route {
    return route(path, HttpMethod.Delete) {
        handle {
            call.respond(body(locations.resolve(TPath::class, call), locations.resolve(TQuery::class, call)))
        }
    }
}

inline fun <reified TResponse : Response, reified TQuery : Any, reified TPath : Any, reified TBody : Any> Route.delete(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TPath, TQuery, TBody) -> TResponse
): Route {
    return route(path, HttpMethod.Delete) {
        handle {
            call.respond(
                body(
                    locations.resolve(TPath::class, call),
                    locations.resolve(TQuery::class, call),
                    call.receive()
                )
            )
        }
    }
}

suspend inline fun ApplicationCall.respond(response: Response) {
    this.response.status(response.status())
    this.response.pipeline.execute(this, SerializableResponse(response))
}

fun Response.status(): HttpStatusCode {
    return when (this) {
        is Response.Error -> HttpStatusCode.UnprocessableEntity
        is Response.Errors -> HttpStatusCode.UnprocessableEntity
        is Response.Either<*, *> -> this.data.fold({ it.status() }, { it.status() }) as HttpStatusCode
        else -> HttpStatusCode.OK
    }
}

class EmptyParams()

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

val Route.openApi: OpenAPI
    get() {
        return this.application.attributes[OpenApiKey]
    }

val OpenApiKey = AttributeKey<OpenAPI>("OpenApi")
