package io.easybreezy.hr.application.location

import io.easybreezy.hr.application.location.queryobject.IsLatestByIdQO
import io.easybreezy.hr.application.location.queryobject.IsLatestByUserIdQO
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import io.easybreezy.infrastructure.query.QueryExecutor
import kotlinx.coroutines.runBlocking
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class Validation @Inject constructor(private val queryExecutor: QueryExecutor) {
    fun onCreateLocation(command: CreateLocation): List<Error> {
        return validate(command) {
            validate(CreateLocation::name).isNotNull().isNotBlank()
            validate(CreateLocation::vacationDays).isNotNull().isGreaterThanOrEqualTo(24)
        }
    }

    fun onAssignLocation(command: AssignLocation): List<Error> {
        return validate(command) {
            validate(AssignLocation::userId).isNotNull()
            validate(AssignLocation::startedAt)
                .isLatestForUser(command.userId)
                .isLessThanOrEqualTo(LocalDate.now().plusDays(1))
            validate(AssignLocation::locationId).isNotNull()
        }
    }

    fun onEditUserLocation(command: EditUserLocation): List<Error> {
        return validate(command) {
            validate(EditUserLocation::startedAt)
                .isLatest(command.userLocationId)
                .isLessThanOrEqualTo(LocalDate.now().plusDays(1))
            validate(EditUserLocation::locationId).isNotNull()
        }
    }

    private object Latest : Constraint {
        override val name: String
            get() = "The user location that is being created isn't the last one"
    }

    private fun <E> Validator<E>.Property<LocalDate?>.isLatestForUser(userId: UUID): Validator<E>.Property<LocalDate?> =
        this.validate(Latest) { value ->
            value == null || runBlocking { queryExecutor.execute(IsLatestByUserIdQO(userId, value)) }
        }

    private fun <E> Validator<E>.Property<LocalDate?>.isLatest(id: UUID): Validator<E>.Property<LocalDate?> =
        this.validate(Latest) { value ->
            value == null || runBlocking { queryExecutor.execute(IsLatestByIdQO(id, value)) }
        }
}
