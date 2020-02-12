package io.easybreezy.hr.model.hr

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class Position private constructor(id: EntityID<UUID>) : UUIDEntity(id)  {
    private var employee by Employee referencedOn Positions.employee
    private var title by Positions.title
    private var since by Positions.since
    private var till by Positions.till

    private var hrManager by Positions.hrManager
    private var createdAt by Positions.createdAt

    companion object : PrivateEntityClass<UUID, Position>(object : Repository() {}) {
        fun define(hrManager: UUID, employee: Employee, title: String, since: LocalDate) = Position.new {
            this.employee = employee
            this.title = title
            this.since = since

            this.hrManager = hrManager
        }
    }

    fun apply(hrManager: UUID, title: String, appliedAt: LocalDate): Position {
        till = appliedAt.minusDays(1)
        return define(hrManager, employee, title, appliedAt)
    }

    fun terminate(terminatedAt: LocalDate) {
        till = terminatedAt
    }

    abstract class Repository : EntityClass<UUID, Position>(
        Positions, Position::class.java
    ) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Position {
            return Position(entityId)
        }
    }
}

object Positions : UUIDTable("employee_positions") {
    val employee = reference("employee_id", Employees)
    val title = varchar("title", 100)
    val since = date("since")
    val till = date("till").nullable()
    val hrManager = uuid("hr_manager_id")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}

