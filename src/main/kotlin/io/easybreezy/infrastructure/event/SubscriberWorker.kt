package io.easybreezy.infrastructure.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

class SubscriberWorker(private val dao: EventsDatabaseAccess, private val eventSubscribers: EventSubscribers) :
    CoroutineScope by CoroutineScope(Dispatchers.Default) {
    val logger = LoggerFactory.getLogger(javaClass)
    suspend fun start(workers: Int = 2) = launch {
        while (isActive) {
            val events = dao.load()
            events.forEach {
                logger.debug("event " + it.second.key.toString() + " was published")
                eventSubscribers.call(it.second)
                dao.publish(it.first)
            }
            delay(10000)
        }
    }
}
