package io.easybreezy.hr.model.hr

import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

class Salary private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var ownerId by Salaries.ownerId
    private var userId by Salaries.userId
    private var reason by Salaries.reason
    private var createdAt by Salaries.createdAt

    companion object : PrivateEntityClass<UUID, Salary>(object : Repository() {}) {
        fun create(ownerId: UUID, userId: UUID, reason: String) = Salary.new {
            this.ownerId = ownerId
            this.userId = userId
            this.reason = reason
            this.createdAt = LocalDateTime.now()
        }
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
    val ownerId = uuid("owner_id")
    val userId = uuid("user_id")
    val reason = varchar("reason", 255)
    val createdAt = datetime("created_at")
}

