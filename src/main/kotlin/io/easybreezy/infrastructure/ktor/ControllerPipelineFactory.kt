package io.easybreezy.infrastructure.ktor

import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Injector
import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext
import kotlin.reflect.KClass

class ControllerPipelineFactory @Inject constructor(private val injector: Injector) : ControllerPipeline {
    override fun <TController : Controller> get(
        clazz: KClass<TController>,
        context: PipelineContext<*, ApplicationCall>
    ): TController {
        val controllerInjector = injector.createChildInjector(object : AbstractModule() {
            override fun configure() {
            }
        })

        val controller = controllerInjector.getInstance(clazz.java)
        controller.pipeline = context

        return controller
    }
}
