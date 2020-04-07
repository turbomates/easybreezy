package io.easybreezy.project.application.team.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
class CloseTeam {
    @Transient
    lateinit var team: UUID
}
