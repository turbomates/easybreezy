package io.easybreezy.hr.model.hr

import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Employee private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {

    private var userId by Employees.userId
    private var fired by Employees.fired

    private var currentPosition by Position referencedOn Employees.currentPosition
    private var currentSalary by Salary referencedOn Employees.currentSalary

    private var creatorId by Employees.creatorId
    private var createdAt by Employees.createdAt

    companion object : PrivateEntityClass<UUID, Employee>(object : Repository() {}) {
        fun hire(creatorId: UUID, userId: UUID, position: String, salary: Int, hiredAt: LocalDate) = Employee.new {
            this.userId = userId
            this.currentPosition = Position.define(creatorId, id, position, hiredAt)
            this.currentSalary = Salary.define(creatorId, id, salary, "", hiredAt)

            this.creatorId = creatorId
        }
    }

    fun fire(firedAt: LocalDate) {
        fired = true
        currentPosition.terminate(firedAt)
        currentSalary.terminate(firedAt)
    }

    fun note(creatorId: UUID, text: String) {
        Note.write(creatorId, id, text)
    }

    fun specifySkills(skills: List<Skill>) {
        skills.forEach { EmployeeSkill.add(id, it) }
    }

    fun promote(promotedBy: UUID, position: String, promotedAt: LocalDate) {
        val promoted = currentPosition.promote(promotedBy, position, promotedAt)
        currentPosition = promoted
    }

    fun raiseSalary(raisedBy: UUID, newAmount: Int, comment: String, raisedAt: LocalDate) {
        val raised = currentSalary.raise(raisedBy, newAmount, comment, raisedAt)
        currentSalary = raised
    }

    abstract class Repository : EntityClass<UUID, Employee>(Employees, Employee::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Employee {
            return Employee(entityId)
        }
    }
}

object Employees: UUIDTable() {
    val userId = uuid("user_id")
    val fired = bool("fired").default(false)

    val currentPosition = reference("current_position_id", Positions)
    val currentSalary = reference("current_salary_id", Salaries)

    val creatorId = uuid("creator_id")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}