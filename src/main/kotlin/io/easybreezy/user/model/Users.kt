package io.easybreezy.user.model

import io.easybreezy.infrastructure.domain.AggregateRoot
import io.easybreezy.infrastructure.events.user.UserInvited
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.user.model_legacy.Token
import kotlinx.serialization.Serializable
import kotlinx.serialization.set
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.postgresql.util.PGobject
import java.util.UUID

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
enum class Role() {
    ADMIN, MEMBER
}

// object Users : Table() {
object Users : UUIDTable() {
    val password = varchar("password", 255).nullable()
    val email = varchar("email_address", 255)
    val firstName = varchar("first_name", 255).nullable()
    val lastName = varchar("last_name", 255).nullable()
    val token = varchar("token", 255).nullable()
    val status = customEnumeration(
        "status",
        "user_status",
        { value -> Status.valueOf(value as String) },
        { PGEnum("user_status", it) }).default(Status.ACTIVE)
    val roles = jsonb("roles", Role.serializer().set)
}

typealias UserId = UUID

class User(private val entity: UserEntity) : AggregateRoot() {


    // val id: UserId by entity.id
    // val email: Email by Users.email
    // var roles: Set<Role>
    // var status: Status
    // var name: Name? = null
    // var token: String? = null

    private lateinit var password: Password

    // fun id() = id
    // fun email() = email.address()
    // fun roles() = roles
    // fun status() = status
    // fun isActive() = status == Status.ACTIVE

    fun invite(email: Email, roles: MutableSet<Role>) {
        entity.email = email.address()
        entity.roles = roles
    }

    fun confirm(password: Password, name: Name) {
        //
        // this.password = password
        // this.name = name
        //
        // entity.firstName = name.firstName
        // entity.lastName = name.lastName
        // entity.password = password.toString()
    }

    //
    // companion object {
    //     fun invite(email: Email, roles: MutableSet<Role>): User {
    //
    //         val user = User(
    //             UUID.randomUUID(),
    //             email,
    //             roles,
    //             Status.WAIT_CONFIRM
    //         )
    //         user.token = Token.generate()
    //         user.addEvent(UserInvited(user.id))
    //
    //         return user
    //     }
    // }
}

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(Users)

    var email by Users.email
    var password by Users.password
    var roles by Users.roles
    var firstName by Users.firstName
    var lastName by Users.lastName
    var status by Users.status
    var token by Users.token

    fun toModel(): User {

        // val user =  User(id.value, Email(email), roles, status, Name(firstName, lastName), token)
        // user.entity = this
        //
        // return user
        return User(this)
    }

    fun fromModel(user: User) {
    }
}
