package io.easybreezy.project.infrastructure

import io.easybreezy.project.model.Project
import io.easybreezy.project.model.Projects
import io.easybreezy.project.model.Repository
import io.easybreezy.project.model.issue.Categories
import io.easybreezy.project.model.issue.Issues
import io.easybreezy.project.model.team.Members
import io.easybreezy.project.model.team.Roles
import io.easybreezy.project.model.team.Teams
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import java.util.*

class ProjectRepository : Project.Repository(), Repository {
    override fun getBySlug(slug: String): Project {
        return find { Projects.slug eq slug }.first()
    }

    override fun hasMembers(withRoleId: UUID): Boolean {
        return Members.select { Members.role eq withRoleId }.count() > 0
    }

    override fun isProjectCategory(category: UUID, project: String): Boolean {
        return Categories
            .join(Projects, JoinType.INNER, Categories.project, Projects.id)
            .select { Categories.id eq category and (Projects.slug eq project)}
            .count() > 0
    }
}
