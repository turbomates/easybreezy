package io.easybreezy.infrastructure.ktor

import com.google.inject.Inject
import com.google.inject.Injector
import kotlin.reflect.KClass

class InterceptorPipelineFactory @Inject constructor(private val injector: Injector) : InterceptorPipeline {
    override fun <TInterceptor : Interceptor> get(clazz: KClass<TInterceptor>): TInterceptor {
        return injector.getInstance(clazz.java)
    }
}
