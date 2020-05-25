package io.easybreezy.project.infrastructure

import io.easybreezy.project.model.issue.Label
import io.easybreezy.project.model.issue.Labels

class LabelRepository : Label.Repository() {

    fun findByNames(names: List<String>): List<Label> {
        return find { Labels.name inList names }.toList()
    }
}
