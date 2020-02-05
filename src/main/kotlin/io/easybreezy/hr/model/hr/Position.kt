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
    private var employee by Positions.employee
    private var title by Positions.title
    private var since by Positions.since
    private var till by Positions.till

    private var creatorId by Positions.creatorId
    private var createdAt by Positions.createdAt

    companion object : PrivateEntityClass<UUID, Position>(object : Repository() {}) {
        fun define(creatorId: UUID, employee: EntityID<UUID>, title: String, since: LocalDate) = Position.new {
            this.employee = employee
            this.title = title
            this.since = since

            this.creatorId = creatorId
        }
    }

    fun promote(promotedBy: UUID, title: String, promotedAt: LocalDate): Position {
        till = promotedAt.minusDays(1)
        return define(promotedBy, employee, title, promotedAt)
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

object Positions : UUIDTable() {
    val employee = reference("employee_id", Employees)
    val title = varchar("title", 100)
    val since = date("since")
    val till = date("till")
    val creatorId = uuid("creator_id")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}

