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

class Salary private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var employee by Employee referencedOn Salaries.employee
    private var amount by Salaries.amount
    private var since by Salaries.since
    private var till by Salaries.till

    private var hrManager by Salaries.hrManager
    private var createdAt by Salaries.createdAt
    private var comment by Salaries.comment

    companion object : PrivateEntityClass<UUID, Salary>(object : Repository() {}) {
        fun define(hrManager: UUID, employee: Employee, amount: Int, comment: String, since: LocalDate) = Salary.new {
            this.hrManager = hrManager
            this.employee = employee
            this.amount = amount
            this.comment = comment
            this.since = since
        }
    }

    fun correct(correctedBy: UUID, corrected: Int) {
        if (correctedBy != hrManager) {
            throw Exception("Only creator could fix salary amount")
        }
        amount = corrected
    }

    fun apply(hrManager: UUID, new: Int, applyComment: String, appliedAt: LocalDate): Salary {
        till = appliedAt.minusDays(1)
        return define(hrManager, employee, new, applyComment, appliedAt)
    }

    fun terminate(terminatedAt: LocalDate) {
        till = terminatedAt
    }

    fun isCurrent(): Boolean {
        return till == null
    }

    abstract class Repository : EntityClass<UUID, Salary>(
        Salaries, Salary::class.java
    ) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Salary {
            return Salary(entityId)
        }
    }
}

object Salaries : UUIDTable("employee_salaries") {

    val employee = reference("employee_id", Employees)
    val amount = integer("amount")
    val since = date("since")
    val till = date("till").nullable()
    val comment = varchar("comment", 500)
    val hrManager = uuid("hr_manager_id")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}
