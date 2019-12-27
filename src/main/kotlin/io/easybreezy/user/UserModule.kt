package io.easybreezy.user

import com.google.inject.AbstractModule
import com.google.inject.Injector
import com.google.inject.Provides
import io.easybreezy.infrastructure.ktor.ControllerPipeline
import io.easybreezy.infrastructure.ktor.ControllerPipelineFactory
import io.easybreezy.infrastructure.ktor.InterceptorPipelineFactory
import io.easybreezy.infrastructure.ktor.InterceptorPipeline
import io.easybreezy.user.api.Router
import io.easybreezy.user.model.Repository

class UserModule : AbstractModule() {
    override fun configure() {
        bind(Router::class.java).asEagerSingleton()
        bind(Repository::class.java).to(io.easybreezy.user.model.UserRepository::class.java)
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
