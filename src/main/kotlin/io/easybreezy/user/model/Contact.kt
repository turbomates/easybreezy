package io.easybreezy.user.model

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.postgresql.PGEnum
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

class Contact private constructor(id: EntityID<UUID>) : UUIDEntity(id)  {
    private var user by User referencedOn Contacts.user
    private var type by Contacts.type
    private var value by Contacts.value

    companion object : PrivateEntityClass<UUID, Contact>(object : Contact.Repository() {}) {
        fun add(user: User, type: Contacts.Type, value: String) = Contact.new {
            this.type = type
            this.value = value
            this.user = user
        }
    }

    abstract class Repository : EntityClass<UUID, Contact>(Contacts, Contact::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Contact {
            return Contact(entityId)
        }
    }
}

object Contacts : UUIDTable() {
    val user = reference("user_id", Users)
    val type = customEnumeration(
        "type",
        "contact_type",
        { value -> Type.valueOf(value as String) },
        { PGEnum("contact_type", it) })
    val value = varchar("value", 100)

    enum class Type {
        PHONE, EMAIL, SKYPE, TELEGRAM, SLACK
    }
}