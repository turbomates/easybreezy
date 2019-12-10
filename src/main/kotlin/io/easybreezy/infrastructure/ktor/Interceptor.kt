package io.easybreezy.infrastructure.ktor

import io.ktor.routing.Route

abstract class Interceptor {
    abstract fun intercept(route: Route)
}
