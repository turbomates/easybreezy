package io.easybreezy.infrastructure.event

interface Event {
    val key: Key<out Event>

    interface Key<T : Event>
}
