package io.easybreezy.hr.model.hr

import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.dao.embedded
import io.easybreezy.infrastructure.exposed.type.jsonb
import kotlinx.serialization.list
import kotlinx.serialization.serializer
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
    private var personalData by Employees.personalData
    private var fired by Employees.fired

    private val positions by Position referrersOn Positions.employee
    private val salaries by Salary referrersOn Salaries.employee
    private val notes by Note referrersOn Notes.employee
    private var skills by Employees.skills

    private var createdAt by Employees.createdAt

    companion object : PrivateEntityClass<UUID, Employee>(object : Repository() {}) {
        fun createCard(userId: UUID, personalData: PersonalData) = Employee.new {
            this.userId = userId
            this.personalData = personalData
        }
    }

    fun fire(hrManager: UUID, comment: String, firedAt: LocalDate) {
        note(hrManager, comment)
        fired = true
        currentPosition()?.terminate(firedAt)
        currentSalary()?.terminate(firedAt)
    }

    fun note(hrManager: UUID, text: String) {
        Note.write(hrManager, this, text)
    }

    fun specifySkills(specified: List<String>) {
        skills = specified
    }

    fun updateBio(updated: String) {
        personalData = PersonalData.create(personalData.birthday, updated)
    }

    fun updateBirthday(updated: LocalDate) {
        personalData = PersonalData.create(updated, personalData.bio)
    }

    fun applyPosition(hrManager: UUID, position: String, appliedAt: LocalDate) {

        currentPosition()?.apply(hrManager, position, appliedAt)
            ?: Position.define(hrManager, this, position, appliedAt)

    }

    fun applySalary(hrManager: UUID, newAmount: Int, comment: String, appliedAt: LocalDate) {

        currentSalary()?.apply(hrManager, newAmount, comment, appliedAt)
            ?: Salary.define(hrManager, this, newAmount, comment, appliedAt)
    }

    private fun currentPosition(): Position? {
        return positions.firstOrNull { it.isCurrent() }
    }

    private fun currentSalary(): Salary? {
        return salaries.firstOrNull { it.isCurrent() }
    }

    abstract class Repository : EntityClass<UUID, Employee>(Employees, Employee::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Employee {
            return Employee(entityId)
        }
    }
}

object Employees : UUIDTable() {
    val userId = uuid("user_id")
    val fired = bool("fired").default(false)
    val skills = jsonb("skills", String.serializer().list).default(listOf())
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val personalData = embedded<PersonalData>(PersonalDataTable)
}

interface Repository {
    fun getByUserId(userId: UUID): Employee
}