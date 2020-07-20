package io.easybreezy.project.model.issue

import io.easybreezy.common.model.File
import io.easybreezy.common.model.Files
import io.easybreezy.infrastructure.event.project.issue.AttachmentRemoved
import io.easybreezy.infrastructure.event.project.issue.AttachmentsUploaded
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

class Attachment private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var issue by Attachments.id
    private var updatedAt by Attachments.updatedAt
    private var files by File via AttachmentFiles

    companion object : PrivateEntityClass<UUID, Attachment>(object : Attachment.Repository() {}) {
        fun ofIssue(
            issue: UUID
        ): Attachment {
            return Attachment.new {
                this.issue = EntityID(issue, Attachments)
            }
        }

        const val BUCKET = "issue-attachment"
    }

    fun add(attachments: List<File>) {
        val updated = files.toMutableList()
        updated.addAll(attachments)
        files = SizedCollection(updated)
        updatedAt = LocalDateTime.now()
        addEvent(AttachmentsUploaded(id.value, attachments.map { it.id.value }, updatedAt))
    }

    fun remove(removed: File) {
        val updated = files.toMutableList()
        updated.remove(removed)
        files = SizedCollection(updated)
        updatedAt = LocalDateTime.now()
        removed.remove()
        addEvent(AttachmentRemoved(id.value, removed.id.value, updatedAt))
    }

    fun get(fileId: UUID): File {
        return files.find { it.id.value == fileId } ?: throw Exception("File $fileId not found!")
    }

    abstract class Repository : EntityClass<UUID, Attachment>(Attachments, Attachment::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Attachment {
            return Attachment(entityId)
        }
    }
}

object Attachments : IdTable<UUID>("issue_attachments") {
    override val id: Column<EntityID<UUID>> = uuid("issue").entityId()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

object AttachmentFiles : Table("issue_attachment_files") {
    val attachment = reference("attachment", Attachments)
    val file = reference("file", Files)
    override val primaryKey = PrimaryKey(attachment, file, name = "issue_attachment_file_pkey")
}
