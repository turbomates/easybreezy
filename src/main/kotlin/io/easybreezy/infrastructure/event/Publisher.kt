package io.easybreezy.infrastructure.event

interface Publisher {
    fun publish(event: Event): Boolean
}
