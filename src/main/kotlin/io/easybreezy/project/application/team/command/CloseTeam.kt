package io.easybreezy.project.application.team.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
class CloseTeam {
    @Transient
    lateinit var team: UUID
}
