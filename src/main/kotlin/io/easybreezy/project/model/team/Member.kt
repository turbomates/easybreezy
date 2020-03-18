package io.easybreezy.project.model.team

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.EmbeddableTable
import io.easybreezy.infrastructure.exposed.dao.Entity
import io.easybreezy.infrastructure.exposed.dao.embedded
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

class Member private constructor(id: EntityID<UUID>) : Entity<UUID>(id) {
    private var user by Members.user
    private var role by Role referencedOn Members.role
    private var info by Members.info
    private var team by Members.team

    companion object : PrivateEntityClass<UUID, Member>(object : Repository() {}) {
        fun create(team: Team, user: UUID, role: Role, info: Info): Member {
            return Member.new {
                this.user = user
                this.team = team.id
                this.role = role
                this.info = info
            }
        }
    }

    abstract class Repository : EntityClass<UUID, Member>(Members, Member::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Member {
            return Member(entityId)
        }
    }
}

class Info private constructor() : Embeddable() {
    private var name by InfoTable.name
    private var username by InfoTable.username
    private var avatar by InfoTable.avatar

    companion object : EmbeddableClass<Info>(Info::class) {
        fun create(name: String, username: String, avatar: String): Info {
            val info = Info()
            info.name = name
            info.username = username
            info.avatar = avatar
            return info
        }

        override fun createInstance(): Info {
            return Info()
        }
    }
}

object InfoTable : EmbeddableTable() {
    val name = varchar("name", 25)
    val username = varchar("username", 25)
    val avatar = varchar("avatar", 25)
}

object Members : UUIDTable() {
    val team = reference("team", Teams)
    val user = uuid("user_id")
    val role = reference("role", Roles)
    val info = embedded<Info>(InfoTable)
}
