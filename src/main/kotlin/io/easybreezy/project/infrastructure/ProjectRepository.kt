package io.easybreezy.project.infrastructure

import io.easybreezy.project.model.Project
import io.easybreezy.project.model.Projects
import io.easybreezy.project.model.Repository
import io.easybreezy.project.model.team.Members
import org.jetbrains.exposed.sql.select
import java.util.*

class ProjectRepository : Project.Repository(), Repository {
    override fun getBySlug(slug: String): Project {
        return find { Projects.slug eq slug }.first()
    }

    override fun hasMembers(withRoleId: UUID): Boolean {
        return Members.select { Members.role eq withRoleId }.count() > 0
    }
}
