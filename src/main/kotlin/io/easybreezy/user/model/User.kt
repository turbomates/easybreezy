package io.easybreezy.user.model

import io.easybreezy.infrastructure.event.user.Confirmed
import io.easybreezy.infrastructure.event.user.Hired
import io.easybreezy.infrastructure.event.user.Invited
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.EmbeddableTable
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.dao.embedded
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.infrastructure.ktor.LogicException
import io.easybreezy.infrastructure.postgresql.PGEnum
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.builtins.set
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

class User private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var email by Users.email
    private var password by Users.password
    private var activities by Users.activities
    private var status by Users.status
    private var token by Users.token
    private var createdAt by Users.createdAt
    private var name by Users.name
    private var comment by Users.comment
    private val contacts by Contact referrersOn Contacts.user

    fun hire() {
        require(status == Status.PENDING) { throw LogicException("User have been already hired") }
        status = Status.WAIT_CONFIRM
        this.addEvent(Hired(this.id.value))
    }

    fun archive(reason: String?) {
        require(status == Status.PENDING) { throw LogicException("Users with status Pending only can be approved") }
        status = Status.ARCHIVED
        comment = reason
    }

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

    fun replaceActivities(activities: Set<String>) {
        this.activities = activities
    }

    fun replaceContacts(replaced: List<io.easybreezy.user.application.Contact>) {
        contacts.forEach { it.delete() }
        replaced.map {
            Contact.add(this, it.type, it.value)
        }
    }

    fun password(): Password {
        return password
    }

    private fun resetToken() {
        token = null
    }

    companion object : PrivateEntityClass<UUID, User>(object : Repository() {}) {
        fun invite(email: Email, activities: Set<String>): User {
            return User.new {
                this.email = email
                this.activities = activities
                this.status = Status.WAIT_CONFIRM
                this.token = Token.generate()
                this.createdAt = LocalDateTime.now()
                this.addEvent(Invited(this.id.value))
            }
        }

        fun create(email: Email, name: Name, activities: Set<String>): User {
            return User.new {
                this.email = email
                this.name = name
                this.activities = activities
                this.status = Status.PENDING
                this.createdAt = LocalDateTime.now()
            }
        }
    }

    class Name private constructor() : Embeddable() {
        private var first by NameTable.firstName
        private var last by NameTable.lastName

        companion object : EmbeddableClass<Name>(Name::class) {
            override fun createInstance(): Name {
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
    PENDING, ARCHIVED, WAIT_CONFIRM, ACTIVE
}

object Users : UUIDTable() {
    val token = varchar("token", 255).nullable()
    val status = customEnumeration(
        "status",
        "user_status",
        { value -> Status.valueOf(value as String) },
        { PGEnum("user_status", it) }).default(Status.ACTIVE)
    val activities = jsonb("activities", String.serializer().set)
    val password = embedded<Password>(PasswordTable)
    val email = embedded<Email>(EmailTable)
    val name = embedded<User.Name>(NameTable)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val comment = text("comment").nullable()
}

object NameTable : EmbeddableTable() {
    val firstName = varchar("first_name", 100).nullable()
    val lastName = varchar("last_name", 100).nullable()
}
