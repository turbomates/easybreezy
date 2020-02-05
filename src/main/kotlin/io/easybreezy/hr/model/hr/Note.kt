package io.easybreezy.hr.model.hr

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

class Note private constructor(id: EntityID<UUID>) : UUIDEntity(id)  {
    private var creatorId by Notes.creatorId
    private var employee by Notes.employee
    private var text by Notes.text
    private var archived by Notes.archived
    private var createdAt by Notes.createdAt

    companion object : PrivateEntityClass<UUID, Note>(object : Repository() {}) {
        fun write(creatorId: UUID, employee: EntityID<UUID>, text: String) = Note.new {
            this.creatorId = creatorId
            this.employee = employee
            this.text = text
        }
    }

    fun correct(correctedBy: UUID, correctedText: String) {
        if (correctedBy !== creatorId) {
            throw Exception("Only creator could correct note")
        }
        text = correctedText
    }

    fun archive(archivedBy: UUID) {
        if (archivedBy !== creatorId) {
            throw Exception("Only creator could archive note")
        }
        archived = true
    }

    abstract class Repository : EntityClass<UUID, Note>(
        Notes, Note::class.java
    ) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Note {
            return Note(entityId)
        }
    }
}

object Notes : UUIDTable() {
    val employee = reference("employee_id", Employees)
    val text = text("text")
    val archived = bool("archived").default(false)
    val creatorId = uuid("creator_id")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}
