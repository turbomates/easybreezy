package io.easybreezy.user.model

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.project.model.team.Embeddable
import io.easybreezy.project.model.team.EmbeddableColumn
import kotlinx.serialization.Serializable
import kotlinx.serialization.set
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.mindrot.jbcrypt.BCrypt
import org.postgresql.util.PGobject
import java.util.UUID
import kotlin.reflect.KProperty

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

    object Name : EmbeddableColumn<User.Name, UUID>() {
        val firstName = Users.varchar("first_name", 25).nullable()
        val lastName = Users.varchar("last_name", 25).nullable()
        override val initializer: Entity<UUID>.(KProperty<*>) -> User.Name =
            { property ->
                val name = User.Name(
                    firstName.getValue(this, property)!!,
                    lastName.getValue(this, property)!!
                )
                name.readValues = this._readValues
                name
            }
    }

    object Password : EmbeddableColumn<User.Password, UUID>() {
        val hashedPassword = Users.varchar("password", 255).nullable()

        override val initializer: Entity<UUID>.(KProperty<*>) -> User.Password =
            { property ->
                val password = User.Password(

                    hashedPassword.getValue(this, property)!!
                )
                password.readValues = this._readValues
                password
            }
    }

    object Email : EmbeddableColumn<User.Email, UUID>() {
        val address = Users.varchar("email_address", 255)

        override val initializer: Entity<UUID>.(KProperty<*>) -> User.Email =
            { property ->
                val email = User.Email(
                    address.getValue(this, property)
                )
                email.readValues = this._readValues
                email
            }
    }
}

class User private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var email by Users.email
    private var password by Users.password
    private var roles by Users.roles
    private var name by Users.name
    private var status by Users.status
    private var token by Users.token

    class Email(address: String) : Embeddable() {
        private var address by Users.Email.address

        init {
            this.address = address
        }
    }

    class Name(firstName: String, lastName: String) : Embeddable() {
        private var firstName by Users.Name.firstName
        private var lastName by Users.Name.lastName

        init {
            this.firstName = firstName
            this.lastName = lastName
        }
    }

    class Password(plainPassword: String) : Embeddable() {
        private var hashedPassword by Users.Password.hashedPassword

        init {
            this.hashedPassword = hash(plainPassword)
        }

        companion object {
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

    fun confirm(password: Password, name: Name) {
        this.password = password
        this.name = name
        this.status = Status.ACTIVE
        resetToken()
    }

    private fun resetToken() {
        token = null
    }

    abstract class Repository : UUIDEntityClass<User>(Users, User::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): User {
            return User(entityId)
        }
    }
}
