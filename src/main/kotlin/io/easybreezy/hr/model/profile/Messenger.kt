package io.easybreezy.hr.model.profile

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.user.model.PGEnum
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

class Messenger private constructor(id: EntityID<UUID>) : Entity<UUID>(id) {
    private var type by Messengers.type
    private var username by Messengers.username
    private var profile by Messengers.profile

    companion object : PrivateEntityClass<UUID, Messenger>(object : Repository() {}) {
        fun create(profile: Profile, type: Messengers.Type, username: String) = Messenger.new {
            this.type = type
            this.username = username
            this.profile = profile.id
        }
    }

    abstract class Repository : EntityClass<UUID, Messenger>(Messengers, Messenger::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Messenger {
            return Messenger(entityId)
        }
    }
}

object Messengers : UUIDTable() {
    val type = customEnumeration(
        "type",
        "messenger_type",
        { value -> Type.valueOf(value as String) },
        { PGEnum("messenger_type", it) })
    val profile = reference("profile", Profiles)
    val username = varchar("username", 255)

    enum class Type {
        SKYPE, TELEGRAM
    }
}
