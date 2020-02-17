package io.easybreezy.hr.application.hr.queryobject

import io.easybreezy.hr.model.hr.Employees
import io.easybreezy.hr.model.hr.Notes
import io.easybreezy.hr.model.hr.Positions
import io.easybreezy.hr.model.hr.Salaries
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class EmployeeDetailsQO(private val userId: UUID) : QueryObject<EmployeeDetails> {
    override suspend fun getData(): EmployeeDetails {
        return transaction {
            (Employees leftJoin Salaries leftJoin Positions leftJoin Notes)
                .select {
                    Employees.userId eq userId
                }.toEmployeeDetailsJoined().single()
        }
    }
}

fun Iterable<ResultRow>.toEmployeeDetailsJoined(): List<EmployeeDetails> {
    return fold(mutableMapOf<UUID, EmployeeDetails>()) { map, resultRow ->
        val details = resultRow.toEmployeeDetails()
        val current = map.getOrDefault(details.userId, details)

        val noteId = resultRow.getOrNull(Notes.id)
        val salaryId = resultRow.getOrNull(Salaries.id)
        val positionId = resultRow.getOrNull(Positions.id)
        val notes = noteId?.let { resultRow.toNote() }
        val salaries = salaryId?.let { resultRow.toSalary() }
        val positions = positionId?.let { resultRow.toPosition() }

        map[details.userId] = current.copy(
            notes = current.notes.plus(listOfNotNull(notes)).distinct(),
            salaries = current.salaries.plus(listOfNotNull(salaries)).distinct(),
            positions = current.positions.plus(listOfNotNull(positions)).distinct()
        )
        map

    }.values.toList()
}

fun ResultRow.toEmployeeDetails() = EmployeeDetails(
    this[Employees.userId],
    this[Employees.firstName],
    this[Employees.lastName],
    this[Employees.skills],
    this[Employees.birthday].toString(),
    this[Employees.bio]
)

fun ResultRow.toNote() = Note(
    this[Notes.id].value,
    this[Notes.text],
    this[Notes.archived],
    this[Notes.hrManager],
    this[Notes.createdAt].toString()
)

fun ResultRow.toSalary() = Salary(
    this[Salaries.id].value,
    this[Salaries.amount],
    this[Salaries.comment],
    this[Salaries.since].toString(),
    this[Salaries.till].toString()
)

fun ResultRow.toPosition() = Position(
    this[Positions.id].value,
    this[Positions.title],
    this[Positions.since].toString(),
    this[Positions.till].toString()
)

@Serializable
data class EmployeeDetails(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val firstName: String,
    val lastName: String,
    val skills: List<String>?,
    val birthday: String?,
    val bio: String?,
    var notes: List<Note> = listOf(),
    var salaries: List<Salary> = listOf(),
    var positions: List<Position> = listOf()
)

@Serializable
data class Note(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val text: String,
    val archived: Boolean,
    @Serializable(with = UUIDSerializer::class)
    val authorId: UUID,
    val createdAt: String
)

@Serializable
data class Position(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val title: String,
    val since: String,
    val till: String?
)

@Serializable
data class Salary(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val amount: Int,
    val comment: String,
    val since: String,
    val till: String?
)