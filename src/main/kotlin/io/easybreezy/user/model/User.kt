package io.easybreezy.user.model

import io.easybreezy.infrastructure.event.user.Confirmed
import io.easybreezy.infrastructure.event.user.Invited
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableColumn
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.type.jsonb
import kotlinx.serialization.Serializable
import kotlinx.serialization.set
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.mindrot.jbcrypt.BCrypt
import org.postgresql.util.PGobject
import java.util.UUID

class User private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var email by Users.email
    private var password by Users.password
    private var roles by Users.roles
    private var name by Users.name
    private var status by Users.status
    private var token by Users.token

    class Email private constructor() : Embeddable() {
        private var address by Users.Email.address

        companion object : EmbeddableClass<Email>(Users) {
            override fun createInstance(): Email {
                return Email()
            }

            fun create(address: String): Email = Email.new {
                this.address = address
            }
        }

        fun address() = address
    }

    class Name private constructor() : Embeddable() {
        private var firstName by Users.Name.firstName
        private var lastName by Users.Name.lastName

        companion object : EmbeddableClass<Name>(Users) {
            override fun createInstance(): Name {
                return Name()
            }

            fun create(firstName: String, lastName: String) = Name.new {
                this.firstName = firstName
                this.lastName = lastName
            }
        }
    }

    class Password private constructor() : Embeddable() {
        private var hashedPassword by Users.Password.hashedPassword

        companion object : EmbeddableClass<Password>(Users) {

            override fun createInstance(): Password {
                return Password()
            }

            fun create(plainPassword: String) = Password.new {
                this.hashedPassword = hash(plainPassword)
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

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
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
    val password = Password
    val email = Email
    val name = Name
    val token = varchar("token", 255).nullable()
    val status = customEnumeration(
        "status",
        "user_status",
        { value -> Status.valueOf(value as String) },
        { PGEnum("user_status", it) }).default(Status.ACTIVE)
    val roles = jsonb("roles", Role.serializer().set)

    object Name : EmbeddableColumn<User.Name>() {
        val firstName = varchar("first_name", 25).nullable()
        val lastName = varchar("last_name", 25).nullable()
    }

    object Password : EmbeddableColumn<User.Password>() {
        val hashedPassword = varchar("password", 255).nullable()
    }

    object Email : EmbeddableColumn<User.Email>() {
        val address = varchar("email_address", 255)
    }
}
