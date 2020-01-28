package io.easybreezy.hr.application.profile.subscriber

import io.easybreezy.hr.model.profile.Profile
import io.easybreezy.infrastructure.event.EventSubscriber
import io.easybreezy.infrastructure.event.user.Invited
import org.jetbrains.exposed.sql.transactions.transaction

class UserInvitedSubscriber : EventSubscriber<Invited> {
    override fun invoke(event: Invited) {
        transaction {
            Profile.create(event.user)
        }
    }
}
