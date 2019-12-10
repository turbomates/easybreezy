package io.easybreezy.infrastructure.ktor

import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext
import kotlin.reflect.KClass

interface ControllerPipeline {
    fun <TController : Controller> get(
        clazz: KClass<TController>,
        context: PipelineContext<*, ApplicationCall>
    ): TController
}
