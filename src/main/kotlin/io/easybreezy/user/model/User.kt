package io.easybreezy.user.model

import io.easybreezy.infrastructure.event.user.Confirmed
import io.easybreezy.infrastructure.event.user.Invited
import io.easybreezy.infrastructure.exposed.dao.*
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.infrastructure.postgresql.PGEnum
import kotlinx.serialization.Serializable
import kotlinx.serialization.set
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

class User private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var email by Embedded(Email)
    private var password by Embedded(Password)
    private var roles by Users.roles
    private var name by Embedded(Name)
    private var status by Users.status
    private var token by Users.token

    class Email private constructor() : Embeddable() {
        private var address by Users.email

        companion object : EmbeddableClass<Email>(Email::class) {
            override fun createInstance(resultRow: ResultRow?): Email {
                return Email()
            }

            fun create(address: String): Email {
                val email = Email()
                email.address = address
                return email
            }
        }

        fun address() = address
    }

    class Name private constructor() : Embeddable() {
        private var firstName by Users.firstName
        private var lastName by Users.lastName

        companion object : EmbeddableClass<Name>(Name::class) {
            override fun createInstance(resultRow: ResultRow?): Name {
                return Name()
            }

            fun create(firstName: String, lastName: String): Name {
                val name = Name()
                name.firstName = firstName
                name.lastName = lastName
                return name
            }
        }
    }

    class Password private constructor() : Embeddable() {
        private var hashedPassword by Users.hashedPassword

        companion object : EmbeddableClass<Password>(Password::class) {
            override fun createInstance(resultRow: ResultRow?): Password {
                return Password()
            }

            fun create(plainPassword: String): Password {
                val password = Password()
                password.hashedPassword = hash(plainPassword)
                return password
            }

            fun verifyPassword(enteredPassword: String, password: String): Boolean {
                return BCrypt.checkpw(enteredPassword, password)
            }

            fun hash(plainPassword: String): String {
                return BCrypt.hashpw(plainPassword, BCrypt.gensalt())
            }
        }

        fun change(newPassword: String) {
            hashedPassword = hash(newPassword)
        }

        fun isValid(enteredPassword: String): Boolean {
            return BCrypt.checkpw(enteredPassword, hashedPassword)
        }
    }

    companion object : PrivateEntityClass<UUID, User>(object : Repository() {}) {
        fun invite(email: Email, roles: MutableSet<Role>): User {
            return User.new {
                this.email = email
                this.roles = roles
                this.status = Status.WAIT_CONFIRM
                this.token = Token.generate()
                this.addEvent(Invited(this.id.value))
            }
        }

        fun createAdmin(email: Email, password: Password, name: Name): User {
            return User.new {
                this.email = email
                this.password = password
                this.name = name
                this.roles = mutableSetOf(Role.ADMIN)
                this.status = Status.ACTIVE
            }
        }
    }

    fun email(): String {
        return this.email.address()
    }

    fun name() = name
    fun token() = token

    fun roles() = roles
    fun status() = status
    fun password() = password

    fun confirm(password: Password, name: Name) {
        this.password = password
        this.name = name
        this.status = Status.ACTIVE
        resetToken()

        this.addEvent(Confirmed(this.id.value))
    }

    private fun resetToken() {
        token = null
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

@Serializable
enum class Role {
    ADMIN, MEMBER
}

object Users : UUIDTable() {
    val token = varchar("token", 255).nullable()
    val status = customEnumeration(
        "status",
        "user_status",
        { value -> Status.valueOf(value as String) },
        { PGEnum("user_status", it) }).default(Status.ACTIVE)
    val roles = jsonb("roles", Role.serializer().set)
    val firstName = varchar("first_name", 25).nullable()
    val lastName = varchar("last_name", 25).nullable()
    val hashedPassword = varchar("password", 255).nullable()
    val email = varchar("email_address", 255)
}
