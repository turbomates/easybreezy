package io.easybreezy.hr.infrastructure

import io.easybreezy.hr.model.profile.Profile
import io.easybreezy.hr.model.profile.Profiles
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import io.easybreezy.hr.model.profile.Repository as RepositoryInterface

class ProfileRepository : Profile.Repository(), RepositoryInterface {
    override fun getByUser(id: UUID): Profile {
        return transaction {
            find { Profiles.userId eq id }.first()
        }
    }
}
