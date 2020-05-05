package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.RemainingTime
import io.easybreezy.hr.application.RemainingTimes
import io.easybreezy.hr.application.VacationQO
import io.easybreezy.hr.application.VacationsQO
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import java.util.UUID

class VacationController @Inject constructor(private val queryExecutor: QueryExecutor) : Controller() {

    suspend fun calculateVacation(userId: UUID): Response.Data<RemainingTime> {
        return Response.Data(queryExecutor.execute(VacationQO(userId)))
    }

    suspend fun calculateVacations(): Response.Data<RemainingTimes> {
        return Response.Data(queryExecutor.execute(VacationsQO()))
    }
}
