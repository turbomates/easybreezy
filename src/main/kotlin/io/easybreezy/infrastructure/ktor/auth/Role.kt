package io.easybreezy.infrastructure.ktor.auth

import kotlinx.serialization.Serializable
import org.valiktor.Constraint
import org.valiktor.Validator

/**
 * This class should contains list of all activities for example PROJECT_LIST, PROJECT_EDIT
 */
@Serializable
enum class Role {
    ADMIN, MEMBER
}

object Roles : Constraint {
    override val name: String
        get() = "Invalid role"
}

fun <E> Validator<E>.Property<Iterable<String>?>.isRoles() {
    this.validate(Roles) { value ->
        value?.all { role ->
            Role.values().any { it.name == role }
        } ?: false
    }
}
