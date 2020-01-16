package io.easybreezy.infrastructure.query

interface QueryObject<out T> {
    fun getData(): T
}
