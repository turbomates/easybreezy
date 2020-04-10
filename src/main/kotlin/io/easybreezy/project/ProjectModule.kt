package io.easybreezy.project

import com.google.inject.AbstractModule
import com.google.inject.Injector
import com.google.inject.Provides
import io.easybreezy.infrastructure.event.GuiceSubscriberFactory
import io.easybreezy.infrastructure.ktor.ControllerPipeline
import io.easybreezy.infrastructure.ktor.ControllerPipelineFactory
import io.easybreezy.infrastructure.ktor.InterceptorPipeline
import io.easybreezy.infrastructure.ktor.InterceptorPipelineFactory
import io.easybreezy.project.api.Router
import io.easybreezy.project.infrastructure.ProjectRepository
import io.easybreezy.project.infrastructure.TeamRepository
import io.easybreezy.project.model.Repository

class ProjectModule : AbstractModule() {
    override fun configure() {
        bind(Router::class.java).asEagerSingleton()
        bind(Repository::class.java).to(ProjectRepository::class.java)
        bind(io.easybreezy.project.model.team.Repository::class.java).to(TeamRepository::class.java)
    }

    @Provides
    fun controllerFactory(injector: Injector): ControllerPipeline {
        return ControllerPipelineFactory(injector)
    }

    @Provides
    fun interceptorFactory(injector: Injector): InterceptorPipeline {
        return InterceptorPipelineFactory(injector)
    }

    @Provides
    fun subscriberFactory(injector: Injector): GuiceSubscriberFactory {
        return GuiceSubscriberFactory(injector)
    }
}
