package io.easybreezy.calendar

import com.google.inject.AbstractModule
import com.google.inject.Injector
import com.google.inject.Provides
import io.easybreezy.calendar.api.Router
import io.easybreezy.infrastructure.ktor.ControllerPipeline
import io.easybreezy.infrastructure.ktor.ControllerPipelineFactory
import io.easybreezy.infrastructure.ktor.InterceptorPipeline
import io.easybreezy.infrastructure.ktor.InterceptorPipelineFactory

class CalendarModule : AbstractModule() {
    override fun configure() {
        bind(Router::class.java).asEagerSingleton()
    }

    @Provides
    fun controllerFactory(injector: Injector): ControllerPipeline {
        return ControllerPipelineFactory(injector)
    }

    @Provides
    fun interceptorFactory(injector: Injector): InterceptorPipeline {
        return InterceptorPipelineFactory(injector)
    }
}
