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

class Note private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var hrManager by Notes.hrManager
    private var employee by Employee referencedOn Notes.employee
    private var text by Notes.text
    private var archived by Notes.archived
    private var createdAt by Notes.createdAt

    companion object : PrivateEntityClass<UUID, Note>(object : Repository() {}) {
        fun write(hrManager: UUID, employee: Employee, text: String) = Note.new {
            this.hrManager = hrManager
            this.employee = employee
            this.text = text
        }
    }

    fun correct(correctedBy: UUID, correctedText: String) {
        if (correctedBy !== hrManager) {
            throw Exception("Only creator could correct note")
        }
        text = correctedText
    }

    fun archive(archivedBy: UUID) {
        if (archivedBy !== hrManager) {
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

object Notes : UUIDTable("employee_notes") {
    val employee = reference("employee_id", Employees)
    val text = text("text")
    val archived = bool("archived").default(false)
    val hrManager = uuid("hr_manager_id")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}
