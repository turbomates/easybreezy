package io.easybreezy.infrastructure.ktor

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.auth.Principal
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.principal
import io.ktor.routing.Route
import io.ktor.util.pipeline.PipelineContext
import java.util.UUID

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
