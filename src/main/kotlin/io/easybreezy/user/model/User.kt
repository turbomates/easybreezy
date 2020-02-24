package io.easybreezy.user.model

import io.easybreezy.infrastructure.event.user.Confirmed
import io.easybreezy.infrastructure.event.user.Invited
import io.easybreezy.infrastructure.exposed.dao.*
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.infrastructure.ktor.auth.Role
import io.easybreezy.infrastructure.postgresql.PGEnum
import kotlinx.serialization.set
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

class User private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var email by Embedded(Email)
    private var password by Embedded(Password)
    private var roles by Users.roles
    private var status by Users.status
    private var token by Users.token
    private var createdAt by Users.createdAt
    private var name by Embedded(Name)
    private val contacts by Contact referrersOn Contacts.user

    fun confirm(password: Password, firstName: String, lastName: String) {
        this.password = password
        this.status = Status.ACTIVE
        this.name = Name.create(firstName, lastName)
        resetToken()

        this.addEvent(Confirmed(this.id.value, firstName, lastName))
    }

    fun email(): String {
        return this.email.address
    }

    fun replaceContacts(replaced: List<io.easybreezy.user.application.Contact>) {
        contacts.forEach { it.delete()}
        replaced.map {
            Contact.add(
                this,
                Contacts.Type.valueOf(it.type),
                it.value
            )
        }
    }

    private fun resetToken() {
        token = null
    }

    companion object : PrivateEntityClass<UUID, User>(object : Repository() {}) {
        fun invite(email: Email, roles: MutableSet<Role>): User {
            return User.new {
                this.email = email
                this.roles = roles
                this.status = Status.WAIT_CONFIRM
                this.token = Token.generate()
                this.createdAt = LocalDateTime.now()
                this.addEvent(Invited(this.id.value))
            }
        }

        fun createAdmin(email: Email, password: Password): User {
            return User.new {
                this.email = email
                this.password = password
                this.roles = mutableSetOf(Role.ADMIN)
                this.status = Status.ACTIVE
            }
        }
    }

    class Name private constructor() : Embeddable() {
        private var first by Users.firstName
        private var last by Users.lastName

        companion object : EmbeddableClass<Name>(
            Name::class) {
            override fun createInstance(resultRow: ResultRow?): Name {
                return Name()
            }

            fun create(firstName: String, lastName: String): Name {
                val name = Name()
                name.first = firstName
                name.last = lastName
                return name
            }
        }
    }

    abstract class Repository : EntityClass<UUID, User>(Users, User::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): User {
            return User(entityId)
        }
    }
}

enum class Status {
    ACTIVE, WAIT_CONFIRM
}

object Users : UUIDTable() {
    val token = varchar("token", 255).nullable()
    val status = customEnumeration(
        "status",
        "user_status",
        { value -> Status.valueOf(value as String) },
        { PGEnum("user_status", it) }).default(Status.ACTIVE)
    val roles = jsonb("roles", Role.serializer().set)
    val hashedPassword = varchar("password", 255).nullable()
    val email = varchar("email_address", 255).uniqueIndex()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val firstName = varchar("first_name", 100).nullable()
    val lastName = varchar("last_name", 100).nullable()
}
