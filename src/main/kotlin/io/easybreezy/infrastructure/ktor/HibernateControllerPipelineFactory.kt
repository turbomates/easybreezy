package io.easybreezy.infrastructure.ktor

import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Injector
import io.easybreezy.infrastructure.ktor.features.HibernateSessionKey
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.util.pipeline.PipelineContext
import org.hibernate.Session
import kotlin.reflect.KClass

class HibernateControllerPipelineFactory @Inject constructor(
    private val injector: Injector,
    private val block: (Session) -> AbstractModule
) : ControllerPipeline {
    override fun <TController : Controller> get(
        clazz: KClass<TController>,
        context: PipelineContext<*, ApplicationCall>
    ): TController {
        val session = context.call.attributes[HibernateSessionKey]
        val controllerInjector = injector.createChildInjector(block(session))
        val controller = controllerInjector.getInstance(clazz.java)
        controller.pipeline = context

        return controller
    }
}
