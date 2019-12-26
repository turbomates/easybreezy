package io.easybreezy.project.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.respondOk
import io.easybreezy.project.model.team.Member
import io.easybreezy.project.model.team.MemberRepository
import io.easybreezy.project.model.team.Members
import io.easybreezy.project.model.team.Role
import io.ktor.response.respondText
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ProjectController @Inject constructor(private val database: Database) : Controller() {

    suspend fun index() {
        transaction(database) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.createMissingTablesAndColumns(Members)
            val role = Role.create(UUID.randomUUID(), "Test")
            Member.create(UUID.randomUUID(), role, Member.Info("", "", ""))
            MemberRepository.wrapRows(Members.selectAll())
        }
        call.respondText("USERS")
    }

    suspend fun invite() {

        call.respondOk()
    }
}
