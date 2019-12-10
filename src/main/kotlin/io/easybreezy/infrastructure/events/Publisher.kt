package io.easybreezy.infrastructure.events

interface Publisher {
    fun publish(event: Event): Boolean
}
