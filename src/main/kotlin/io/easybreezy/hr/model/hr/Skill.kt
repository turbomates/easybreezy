package io.easybreezy.hr.model.hr

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.Embedded
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

class Skill private constructor() : Embeddable() {
    private var title by EmployeeSkills.skill

    companion object : EmbeddableClass<Skill>(Skill::class) {
        override fun createInstance(resultRow: ResultRow?): Skill {
            return Skill()
        }

        fun create(title: String): Skill {
            val skill = Skill()
            skill.title = title
            return skill
        }
    }
}

class EmployeeSkill private constructor(id: EntityID<UUID>) : UUIDEntity(id)  {
    private var employee by EmployeeSkills.employee
    private var skill by Embedded(Skill)

    companion object : PrivateEntityClass<UUID, EmployeeSkill>(object : Repository() {}) {
        fun add(employee: EntityID<UUID>, skill: Skill) = EmployeeSkill.new {
            this.employee = employee
            this.skill = skill
        }
    }

    abstract class Repository : UUIDEntityClass<EmployeeSkill>(EmployeeSkills, EmployeeSkill::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): EmployeeSkill {
            return EmployeeSkill(entityId)
        }
    }
}

object EmployeeSkills: UUIDTable() {
    val employee = reference("employee_id", Employees)
    val skill = varchar("skill", 100)
}