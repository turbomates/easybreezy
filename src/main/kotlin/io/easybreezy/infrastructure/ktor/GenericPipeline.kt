package io.easybreezy.infrastructure.ktor

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext
import kotlin.reflect.KClass

class GenericPipeline @Inject constructor(
    private val controllerPipeline: ControllerPipeline,
    private val interceptorPipeline: InterceptorPipeline
) {
    fun <TController : Controller> controller(
        clazz: KClass<TController>,
        context: PipelineContext<*, ApplicationCall>
    ): TController {
        return controllerPipeline.get(clazz, context)
    }

    fun <TInterceptor : Interceptor> interceptor(clazz: KClass<TInterceptor>): TInterceptor {
        return interceptorPipeline.get(clazz)
    }
}
