package io.easybreezy.integration.openapi.ktor

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.feature
import io.ktor.http.HttpMethod
import io.ktor.locations.locations
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.application
import io.ktor.routing.method
import io.ktor.routing.route
import io.ktor.util.pipeline.PipelineContext
import kotlin.reflect.typeOf

inline fun <reified TResponse : Any> Route.post(
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> TResponse
): Route {
    return method(HttpMethod.Post) {
        handle {
            call.respond(body())
        }
    }
}

inline fun <reified TResponse : Any> Route.post(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Post,
        typeOf<TResponse>()
    )
    return route(path, HttpMethod.Post) {
        handle {
            call.respond(body())
        }
    }
}

inline fun <reified TResponse : Any, reified TBody : Any> Route.post(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TBody) -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Post,
        typeOf<TResponse>(),
        typeOf<TBody>()
    )
    return route(path, HttpMethod.Post) {
        handle {
            call.respond(body(call.receive()))
        }
    }
}

inline fun <reified TResponse : Any, reified TParams : Any> Route.postParams(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TParams) -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Post,
        typeOf<TResponse>(),
        pathParams = typeOf<TParams>()
    )
    return route(path, HttpMethod.Post) {
        handle {
            call.respond(body(locations.resolve(TParams::class, call)))
        }
    }
}

inline fun <reified TResponse : Any, reified TBody : Any, reified TParams : Any> Route.post(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TBody, TParams) -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Post,
        typeOf<TResponse>(),
        typeOf<TBody>(),
        typeOf<TParams>()
    )
    return route(path, HttpMethod.Post) {
        handle {
            call.respond(body(call.receive(), locations.resolve(TParams::class, call)))
        }
    }
}

inline fun <reified TResponse : Any, reified TBody : Any, reified TQuery : Any, reified TPath : Any> Route.post(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TBody, TPath, TQuery) -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Post,
        typeOf<TResponse>(),
        typeOf<TBody>(),
        typeOf<TQuery>()
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

inline fun <reified TResponse : Any> Route.get(
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> TResponse
): Route {
    return method(HttpMethod.Get) {
        handle {
            call.respond(body())
        }
    }
}

inline fun <reified TResponse : Any> Route.get(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Get,
        typeOf<TResponse>()
    )
    return route(path, HttpMethod.Get) {
        handle {
            call.respond(body())
        }
    }
}

inline fun <reified TResponse : Any, reified TParams : Any> Route.get(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TParams) -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Get,
        typeOf<TResponse>(),
        null,
        typeOf<TParams>()
    )
    return route(path, HttpMethod.Get) {
        handle {
            call.respond(body(locations.resolve(TParams::class, call)))
        }
    }
}

inline fun <reified TResponse : Any, reified TQuery : Any, reified TPath : Any> Route.get(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TPath, TQuery) -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Get,
        typeOf<TResponse>(),
        typeOf<TQuery>(),
        typeOf<TPath>()
    )
    return route(path, HttpMethod.Get) {
        handle {
            call.respond(body(locations.resolve(TPath::class, call), locations.resolve(TQuery::class, call)))
        }
    }
}

inline fun <reified TResponse : Any> Route.delete(
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> TResponse
): Route {
    openApi.addPath(
        buildFullPath(),
        HttpMethod.Delete,
        typeOf<TResponse>()
    )
    return method(HttpMethod.Delete) {
        handle {
            call.respond(body())
        }
    }
}

inline fun <reified TResponse : Any> Route.delete(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Delete,
        typeOf<TResponse>()
    )
    return route(path, HttpMethod.Delete) {
        handle {
            call.respond(body())
        }
    }
}

inline fun <reified TResponse : Any, reified TParams : Any> Route.delete(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TParams) -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Delete,
        typeOf<TResponse>(),
        null,
        typeOf<TParams>()
    )
    return route(path, HttpMethod.Delete) {
        handle {
            call.respond(body(locations.resolve(TParams::class, call)))
        }
    }
}

inline fun <reified TResponse : Any, reified TBody : Any> Route.deleteWithBody(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TBody) -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Delete,
        typeOf<TResponse>(),
        typeOf<TBody>()
    )
    return route(path, HttpMethod.Delete) {
        handle {
            call.respond(body(call.receive()))
        }
    }
}

inline fun <reified TResponse : Any, reified TQuery : Any, reified TPath : Any> Route.delete(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TPath, TQuery) -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Delete,
        typeOf<TResponse>(),
        null,
        typeOf<TPath>()//typeOf<TQuery>(),
    )
    return route(path, HttpMethod.Delete) {
        handle {
            call.respond(body(locations.resolve(TPath::class, call), locations.resolve(TQuery::class, call)))
        }
    }
}

inline fun <reified TResponse : Any, reified TQuery : Any, reified TPath : Any, reified TBody : Any> Route.delete(
    path: String,
    noinline body: suspend PipelineContext<Unit, ApplicationCall>.(TPath, TQuery, TBody) -> TResponse
): Route {
    openApi.addPath(
        buildFullPath() + path,
        HttpMethod.Delete,
        typeOf<TResponse>(),
        typeOf<TBody>(),
        typeOf<TPath>()//typeOf<TQuery>(),
    )
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

fun Route.buildFullPath(): String {
    return this.toString().replace(Regex("\\/\\(.*?\\)"), "")
}

val Route.openApi: OpenApi
    get() {
        return this.application.feature(OpenApi)
    }
