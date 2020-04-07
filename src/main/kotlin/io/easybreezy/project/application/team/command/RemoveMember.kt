package io.easybreezy.project.application.team.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
class RemoveMember {
    @Transient
    lateinit var memberId: UUID
    @Transient
    lateinit var team: UUID
}
