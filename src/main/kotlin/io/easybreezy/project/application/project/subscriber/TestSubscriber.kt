package io.easybreezy.project.application.project.subscriber

import com.google.inject.Inject
import io.easybreezy.infrastructure.event.EventSubscriber
import io.easybreezy.infrastructure.event.project.project.Created
import io.easybreezy.project.model.team.Team

class TestSubscriber @Inject constructor(repository: Team.Repository) : EventSubscriber<Created> {
    override fun invoke(event: Created) {
        println("it's working")
    }
}