package io.easybreezy.infrastructure.ktor

import kotlin.reflect.KClass

interface InterceptorPipeline {
    fun <TInterceptor : Interceptor> get(clazz: KClass<TInterceptor>): TInterceptor
}
