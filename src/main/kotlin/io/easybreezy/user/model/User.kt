package io.easybreezy.user.model

import io.easybreezy.application.db.hibernate.type.JsonBType
import io.easybreezy.application.db.hibernate.type.PostgresSQLEnumType
import io.easybreezy.infrastructure.domain.AggregateRoot
import io.easybreezy.infrastructure.events.user.UserCreated
import io.easybreezy.infrastructure.events.user.UserInvited
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

typealias UserId = UUID

@Entity
@TypeDefs(
    value = [
        TypeDef(name = "pgsql_enum", typeClass = PostgresSQLEnumType::class),
        TypeDef(name = "jsonb", typeClass = JsonBType::class)
    ]
)
@Table(name = "users")
class User(
    private val email: Email,
    private var password: Password,
    @Type(type = "jsonb")
    @Column(name = "roles", nullable = true, columnDefinition = "jsonb")
    protected var roles: MutableSet<Role>,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "softswiss_game_status")
    @Type(type = "pgsql_enum")
    private var status: Status
) : AggregateRoot() {

    @Id
    private val id: UserId = UserId.randomUUID()

    private lateinit var name: Name

    companion object {
        fun create(email: Email, password: Password, name: Name, roles: MutableSet<Role>): User {
            val user = User(email, password, roles, Status.ACTIVE)
            user.name = name
            user.addEvent(UserCreated(user.id))

            return user
        }

        fun invite(email: Email, password: Password, roles: MutableSet<Role>): User {
            val user = User(email, password, roles, Status.WAIT_CONFIRM)
            user.addEvent(UserInvited(user.id))

            return user
        }
    }

    fun rename(name: Name) {
        this.name = name
    }

    fun changePassword(password: Password) {
        this.password = password
    }

    enum class Status {
        ACTIVE, WAIT_CONFIRM
    }

    enum class Role() {
        ADMIN, MEMBER
    }
}
