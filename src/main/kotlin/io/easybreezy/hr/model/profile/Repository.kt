package io.easybreezy.hr.model.profile

import java.util.UUID

interface Repository {
    fun getByUser(id: UUID): Profile
}
