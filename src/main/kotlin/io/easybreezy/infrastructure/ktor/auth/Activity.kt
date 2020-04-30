package io.easybreezy.infrastructure.ktor.auth

import kotlinx.serialization.Serializable
import org.valiktor.Constraint
import org.valiktor.Validator

@Serializable
enum class Activity {
    USERS_MANAGE,

    ABSENCES_MANAGE,
    ABSENCES_LIST,

    LOCATIONS_MANAGE,
    LOCATIONS_LIST,

    USER_LOCATIONS_MANAGE,
    USER_LOCATIONS_LIST,

    EMPLOYEES_LIST,
    EMPLOYEES_MANAGE,

    CALENDARS_MANAGE,
    CALENDARS_LIST,

    HOLIDAYS_MANAGE,

    VACATIONS_SHOW,

    PROJECTS_MANAGE,
    PROJECTS_SHOW,
}

object Activities : Constraint {
    override val name: String
        get() = "Invalid activity"
}

fun <E> Validator<E>.Property<Iterable<String>?>.isActivities() {
    this.validate(Activities) { value ->
        value?.all { activity ->
            Activity.values().any { it.name == activity }
        } ?: false
    }
}
