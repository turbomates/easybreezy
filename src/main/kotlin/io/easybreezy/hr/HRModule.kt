package io.easybreezy.hr

import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Provides
import io.easybreezy.hr.api.Router
import io.easybreezy.hr.application.profile.subscriber.ProfileSubscriber
import io.easybreezy.hr.infrastructure.ProfileRepository
import io.easybreezy.hr.model.profile.Repository
import io.easybreezy.infrastructure.event.EventSubscribers
import io.easybreezy.infrastructure.event.GuiceSubscriberFactory
import io.easybreezy.infrastructure.ktor.ControllerPipeline
import io.easybreezy.infrastructure.ktor.ControllerPipelineFactory
import io.easybreezy.infrastructure.ktor.InterceptorPipeline
import io.easybreezy.infrastructure.ktor.InterceptorPipelineFactory

class HRModule : AbstractModule() {
    override fun configure() {
        bind(SubscriberDescription::class.java).asEagerSingleton()
        bind(Router::class.java).asEagerSingleton()
        bind(Repository::class.java).to(ProfileRepository::class.java)
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

class SubscriberDescription @Inject constructor(eventSystem: EventSubscribers) {
    init {
        eventSystem.subscribe(ProfileSubscriber())
    }
}
