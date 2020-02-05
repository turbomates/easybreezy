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

class Salary private constructor(id: EntityID<UUID>) : UUIDEntity(id)  {
    private var employee by Salaries.employee
    private var amount by Salaries.amount
    private var since by Salaries.since
    private var till by Salaries.till

    private var creatorId by Salaries.creatorId
    private var createdAt by Salaries.createdAt
    private var comment by Salaries.comment

    companion object : PrivateEntityClass<UUID, Salary>(object : Repository() {}) {
        fun define(creatorId: UUID, employee: EntityID<UUID>, amount: Int, comment: String, since: LocalDate) = Salary.new {
            this.creatorId = creatorId
            this.employee = employee
            this.amount = amount
            this.comment = comment
            this.since = since
        }
    }

    fun fix(fixedBy: UUID, fixed: Int) {
        if (fixedBy != creatorId) {
            throw Exception("Only creator could fix salary amount")
        }
        amount = fixed
    }

    fun raise(raisedBy: UUID, new: Int, raiseComment: String, raisedAt: LocalDate) : Salary {
        till = raisedAt.minusDays(1)
        return Salary.new {
            this.creatorId = raisedBy
            this.employee = employee
            this.amount = new
            this.comment = raiseComment
            this.since = raisedAt
            this.createdAt = LocalDateTime.now()
        }
    }

    fun terminate(terminatedAt: LocalDate) {
        till = terminatedAt
    }

    abstract class Repository : EntityClass<UUID, Salary>(
        Salaries, Salary::class.java
    ) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Salary {
            return Salary(entityId)
        }
    }
}

object Salaries : UUIDTable() {

    val employee = reference("employee_id", Employees)
    val amount = integer("amount")
    val since = date("since")
    val till = date("till")

    val comment = varchar("comment", 500)
    val creatorId = uuid("creator_id")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}

