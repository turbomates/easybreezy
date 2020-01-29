package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.location.CreateLocation
import io.easybreezy.hr.application.location.Handler
import io.easybreezy.hr.application.location.Validation
import io.easybreezy.hr.application.location.queryobject.LocationsQO
import io.easybreezy.hr.infrastructure.LocationRepository
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.respondListing
import io.easybreezy.infrastructure.ktor.respondOk
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class LocationController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val repository: LocationRepository,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun createLocation(command: CreateLocation) {
        validation.onCreateLocation(command)
        transaction {
            handler.handleCreateLocation(command)
        }

        call.respondOk()
    }

    suspend fun removeLocation(id: UUID) {
        repository.remove(id)

        call.respondOk()
    }

    suspend fun locations() {
        call.respondListing(
            queryExecutor.execute(LocationsQO(call.request.pagingParameters()))
        )
    }
}