package io.easybreezy.infrastructure.ktor.auth

import kotlinx.serialization.Serializable

/**
 * This class should contains list of all activities for example PROJECT_LIST, PROJECT_EDIT
 */
@Serializable
enum class Role {
    ADMIN, MEMBER
}
