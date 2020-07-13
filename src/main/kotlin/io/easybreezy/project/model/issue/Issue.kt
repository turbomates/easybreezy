package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.event.project.issue.CategoryChanged
import io.easybreezy.infrastructure.event.project.issue.Created
import io.easybreezy.infrastructure.event.project.issue.PriorityUpdated
import io.easybreezy.infrastructure.event.project.issue.SubIssueCreated
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
import kotlinx.serialization.builtins.set
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.serializer

class Issue private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var number by Issues.number
    private var category by Issues.category
    private var author by Issues.author
    private var updatedAt by Issues.updatedAt
    private var project by Issues.project
    private var title by Issues.title
    private var description by Issues.description
    private var priority by Issues.priority
    private val comments by Comment referrersOn Comments.issue
    private var parent by Issue optionalReferencedOn Issues.parent
    private var files by Issues.files

    companion object : PrivateEntityClass<UUID, Issue>(object : Issue.Repository() {}) {
        fun planIssue(
            author: UUID,
            project: UUID,
            title: String,
            description: String,
            number: Int,
            priority: Priority?,
            category: UUID?
        ): Issue {
            return Issue.new {
                this.author = author
                this.project = project
                this.title = title
                this.description = description
                this.number = number
                this.category = category
                this.priority = priority ?: Priority.neutral()
                addEvent(Created(this.project, this.id.value, this.author, this.title, this.description, LocalDateTime.now()))
            }
        }
    }

    fun extractSubIssue(
        author: UUID,
        title: String,
        description: String,
        number: Int,
        priority: Priority?,
        category: UUID?
    ): Issue {
        val issue = this
        return Issue.new {
            this.author = author
            this.project = issue.project
            this.number = number
            this.title = title
            this.description = description
            this.category = category
            this.priority = priority ?: Priority.neutral()
            this.parent = issue
            addEvent(SubIssueCreated(this.project, issue.id.value, this.id.value, this.author, this.title, this.description, LocalDateTime.now()))
        }
    }

    fun changeCategory(updated: UUID) {
        updatedAt = LocalDateTime.now()
        addEvent(CategoryChanged(this.id.value, category, updated, updatedAt))
        category = updated
    }

    fun updatePriority(updated: Priority) {
        updatedAt = LocalDateTime.now()
        addEvent(PriorityUpdated(this.id.value, updated.value, updatedAt))
        priority = updated
    }

    fun project() = project

    suspend fun addFiles(files: List<File>, fileStorage: FileStorage) {
        val newFiles = mutableSetOf<Path>()
        files.forEach {
            val path = fileStorage.add(it, id.value)
            newFiles.add(path)
        }
        this.files = this.files.plus(newFiles)
    }

    suspend fun removeFile(path: Path, fileStorage: FileStorage) {
        this.files = this.files.minus(path)
        fileStorage.remove(id.value, path)
    }

    abstract class Repository : EntityClass<UUID, Issue>(Issues, Issue::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Issue {
            return Issue(entityId)
        }
    }
}

object Issues : UUIDTable("issues") {
    val category = uuid("category").nullable()
    val number = integer("number")
    val author = uuid("author")
    val project = uuid("project")
    val title = varchar("title", 255)
    val description = text("description")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val priority = embedded<Priority>(PriorityTable)
    val parent = reference("parent", Issues).nullable()
    val files = jsonb("files", String.serializer().set).default(setOf())
}
