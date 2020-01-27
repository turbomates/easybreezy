package io.easybreezy.project.model.team

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.Embedded
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

class Member private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var user by Members.user
    private var role by Role referencedOn Members.role
    private var info by Embedded(Info)
    private var team by Members.team

    class Info private constructor() : Embeddable() {
        private var name by Members.name
        private var username by Members.username
        private var avatar by Members.avatar

        companion object : EmbeddableClass<Info>(Info::class) {
            override fun createInstance(resultRow: ResultRow?): Info {
                return Info()
            }

            fun create(name: String, username: String, avatar: String): Info {
                val info = Info()
                info.name = name
                info.username = username
                info.avatar = avatar
                return info
            }
        }
    }

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

    abstract class Repository : UUIDEntityClass<Member>(Members, Member::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Member {
            return Member(entityId)
        }
    }
}

object Members : UUIDTable() {
    val team = reference("team", Teams)
    val user = uuid("user_id")
    val role = reference("role", Roles)
    val name = varchar("info_name", 25)
    val username = varchar("info_username", 25)
    val avatar = varchar("info_avatar", 25)
}
