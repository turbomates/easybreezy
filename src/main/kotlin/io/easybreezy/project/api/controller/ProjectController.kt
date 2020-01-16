package io.easybreezy.project.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.ktor.response.respondText
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class ProjectController @Inject constructor(private val database: Database) : Controller() {

    suspend fun create() {
        transaction(database) {
            addLogger(StdOutSqlLogger)
        }
        call.respondText("")
    }
}
