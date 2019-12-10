package io.easybreezy.user

import com.google.inject.AbstractModule
import com.google.inject.Injector
import com.google.inject.Provides
import io.easybreezy.infrastructure.ktor.ControllerPipeline
import io.easybreezy.infrastructure.ktor.HibernateControllerPipelineFactory
import io.easybreezy.infrastructure.ktor.InterceptorPipelineFactory
import io.easybreezy.infrastructure.ktor.InterceptorPipeline
import io.easybreezy.user.api.Router
import io.easybreezy.user.model.Repository
import org.hibernate.Session

class UserModule : AbstractModule() {
    override fun configure() {
        bind(Router::class.java).asEagerSingleton()
    }

    @Provides
    fun controllerFactory(injector: Injector): ControllerPipeline {
        return HibernateControllerPipelineFactory(injector) { session: Session ->
            return@HibernateControllerPipelineFactory object : AbstractModule() {
                override fun configure() {
                    bind(Session::class.java).toInstance(session)
                    bind(Repository::class.java).to(io.easybreezy.user.infrastructure.Repository::class.java)
                }
            }
        }
    }

    @Provides
    fun interceptorFactory(injector: Injector): InterceptorPipeline {
        return InterceptorPipelineFactory(injector)
    }
}
