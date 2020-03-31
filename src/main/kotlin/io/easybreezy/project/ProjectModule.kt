package io.easybreezy.project

import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Provides
import io.easybreezy.infrastructure.event.EventSubscribers
import io.easybreezy.infrastructure.event.GuiceSubscriberFactory
import io.easybreezy.infrastructure.ktor.ControllerPipeline
import io.easybreezy.infrastructure.ktor.ControllerPipelineFactory
import io.easybreezy.infrastructure.ktor.InterceptorPipeline
import io.easybreezy.infrastructure.ktor.InterceptorPipelineFactory
import io.easybreezy.project.api.Router
import io.easybreezy.project.application.project.subscriber.TestSubscriber
import io.easybreezy.project.infrastructure.ProjectRepository
import io.easybreezy.project.infrastructure.TeamRepository
import io.easybreezy.project.model.Repository
import io.easybreezy.project.model.team.Team

class ProjectModule : AbstractModule() {
    override fun configure() {
        bind(SubscriberDescription::class.java).asEagerSingleton()
        bind(Router::class.java).asEagerSingleton()
        bind(Repository::class.java).to(ProjectRepository::class.java)
        bind(Team.Repository::class.java).to(TeamRepository::class.java)
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

class SubscriberDescription @Inject constructor(eventSystem: EventSubscribers, subscriberFactory: GuiceSubscriberFactory) {
    init {
        eventSystem.subscribe(subscriberFactory.get(TestSubscriber::class))
    }
}
