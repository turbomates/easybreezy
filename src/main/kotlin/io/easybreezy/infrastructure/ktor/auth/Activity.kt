package io.easybreezy.infrastructure.ktor.auth

import kotlinx.serialization.Serializable
import org.valiktor.Constraint
import org.valiktor.Validator

@Serializable
enum class Activity {
    USERS_LIST,
    USERS_MANAGE,

    ABSENCES_SELF_MANAGE,
    ABSENCES_MANAGE,
    ABSENCES_LIST,

    LOCATIONS_MANAGE,
    LOCATIONS_LIST,
    USER_LOCATIONS_SELF_MANAGE,
    USER_LOCATIONS_MANAGE,
    USER_LOCATIONS_LIST,

    EMPLOYEES_LIST,
    EMPLOYEES_SELF_MANAGE,
    EMPLOYEES_MANAGE,

    CALENDARS_MANAGE,
    CALENDARS_LIST,

    HOLIDAYS_MANAGE,

    VACATIONS_SHOW_MY,
    VACATIONS_SHOW_ANY,

    PROJECTS_CREATE,
    PROJECTS_MANAGE,
    PROJECTS_ROLES_MANAGE,
    PROJECTS_CATEGORIES_MANAGE,
    PROJECTS_SHOW_MY,
    PROJECTS_SHOW_ANY,

    TEAMS_CREATE,
    TEAMS_ADD_MEMBERS,
    TEAMS_MANAGE,
    TEAMS_SHOW_MY,
    TEAMS_SHOW_ANY,
    TEAMS_MEMBERS_MANAGE,
}

object Activities : Constraint {
    override val name: String
        get() = "Not enough permissions"
}

fun <E> Validator<E>.Property<Iterable<String>?>.isActivities() {
    this.validate(Activities) { value ->
        value?.all { activity ->
            Activity.values().any { it.name == activity }
        } ?: false
    }
}
