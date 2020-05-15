package io.easybreezy.user

import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Provides
import io.easybreezy.infrastructure.event.EventSubscribers
import io.easybreezy.infrastructure.event.GuiceSubscriberFactory
import io.easybreezy.infrastructure.ktor.ControllerPipeline
import io.easybreezy.infrastructure.ktor.ControllerPipelineFactory
import io.easybreezy.infrastructure.ktor.InterceptorPipelineFactory
import io.easybreezy.infrastructure.ktor.InterceptorPipeline
import io.easybreezy.user.api.Router
import io.easybreezy.user.application.command.Handler
import io.easybreezy.user.application.subscriber.EmployeeSubscriber
import io.easybreezy.user.model.Repository

class UserModule : AbstractModule() {
    override fun configure() {
        bind(SubscriberDescription::class.java).asEagerSingleton()
        bind(Router::class.java).asEagerSingleton()
        bind(Repository::class.java).to(io.easybreezy.user.infrastructure.UserRepository::class.java)
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

class SubscriberDescription @Inject constructor(eventSystem: EventSubscribers, handler: Handler) {
    init {
        eventSystem.subscribe(EmployeeSubscriber(handler))
    }
}
