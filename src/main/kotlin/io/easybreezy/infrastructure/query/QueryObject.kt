package io.easybreezy.infrastructure.query

interface QueryObject<out T> {
    suspend fun getData(): T
}
