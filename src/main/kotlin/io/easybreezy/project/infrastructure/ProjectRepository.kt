package io.easybreezy.project.infrastructure

import io.easybreezy.project.model.Project
import io.easybreezy.project.model.Projects
import io.easybreezy.project.model.Repository

class ProjectRepository : Project.Repository(), Repository {
    override fun getBySlug(slug: String): Project {
        return find { Projects.slug eq slug }.first()
    }
}
