package io.easybreezy.user.model

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.project.model.team.Embeddable
import io.easybreezy.project.model.team.EmbeddableColumns
import io.easybreezy.user.model_legacy.Token
import kotlinx.serialization.Serializable
import kotlinx.serialization.set
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.postgresql.util.PGobject
import java.util.UUID
import io.easybreezy.user.model.Repository as RepositoryInterface

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
    val password = varchar("password", 255).nullable()
    val email = varchar("email_address", 255)
    val name = Name
    val token = varchar("token", 255).nullable()
    val status = customEnumeration(
        "status",
        "user_status",
        { value -> Status.valueOf(value as String) },
        { PGEnum("user_status", it) }).default(Status.ACTIVE)
    val roles = jsonb("roles", Role.serializer().set)

    object Name : EmbeddableColumns() {
        val firstName = Users.varchar("first_name", 25).nullable()
        val lastName = Users.varchar("last_name", 25).nullable()
    }
}

class User private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var email by Users.email
    private var password by Users.password
    private var roles by Users.roles
    private var name by Users.name
    private var status by Users.status
    private var token by Users.token

    class Name(firstName: String, lastName: String) : Embeddable() {
        private var firstName by Users.Name.firstName
        private var lastName by Users.Name.lastName

        init {
            this.firstName = firstName
            this.lastName = lastName
        }
    }

    companion object : PrivateEntityClass<UUID, User>(UserRepository()) {
    // companion object : PrivateEntityClass<UUID, User>(Repository) {
        fun invite(email: Email, roles: MutableSet<Role>): User {
            return User.new {
                this.email = email.address()
                this.roles = roles
                this.status = Status.WAIT_CONFIRM
                this.token = Token.generate()
            }
        }
    }

    fun confirm(password: Password, name: Name) {
        // this.password = password
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

class UserRepository : User.Repository(), RepositoryInterface {
    override fun findByToken(token: String): User {
        return find { Users.token eq token }.first()
    }
}
