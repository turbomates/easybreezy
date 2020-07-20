package io.easybreezy.common.model

import io.easybreezy.infrastructure.event.file.FileAdded
import io.easybreezy.infrastructure.event.file.FileRemoved
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.dao.embedded
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

class File private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {

    private var title by Files.title
    private var extension by Files.extension
    var location by Files.location
        private set

    companion object : PrivateEntityClass<UUID, File>(object : File.Repository() {}) {
        fun add(title: String, extension: String, location: FileLocation) = File.new {
            this.title = title
            this.extension = extension
            this.location = location
            addEvent(FileAdded(this.id.value, location.path))
        }
    }

    fun remove() {
        addEvent(FileRemoved(id.value, location.path))
        this.delete()
    }

    abstract class Repository : EntityClass<UUID, File>(Files, File::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): File {
            return File(entityId)
        }
    }
}

object Files : UUIDTable("files") {
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val title = varchar("title", 255)
    val location = embedded<FileLocation>(FileLocationTable)
    val extension = varchar("extension", 25)
}
