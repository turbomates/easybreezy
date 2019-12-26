package io.easybreezy.project.model

import io.easybreezy.project.model.team.Team
import java.util.*

class Project(
    private val name: String
) {
    private val id: UUID = UUID.randomUUID()
    private val slug: Slug = Slug()
    private val description: String = ""
    private val roles: List<String> = emptyList()
    private val statuses: List<Status> = emptyList()
    private val teams: List<Team> = emptyList()
    private val status: Status = Status.Active

    enum class Status {
        Active,
        Closed,
        Suspended
    }

    class Slug {

    }
}