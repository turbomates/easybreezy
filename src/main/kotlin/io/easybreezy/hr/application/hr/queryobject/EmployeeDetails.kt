package io.easybreezy.hr.application.hr.queryobject

import io.easybreezy.hr.model.hr.Employees
import io.easybreezy.hr.model.hr.Notes
import io.easybreezy.hr.model.hr.PersonalDataTable
import io.easybreezy.hr.model.hr.Positions
import io.easybreezy.hr.model.hr.Salaries
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import io.easybreezy.infrastructure.serialization.LocalDateTimeSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.user.model.Contacts
import io.easybreezy.user.model.NameTable
import io.easybreezy.user.model.Users
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class EmployeeDetailsQO(private val userId: UUID) : QueryObject<EmployeeDetails> {
    override suspend fun getData() =
        Employees
            .leftJoin(Salaries)
            .leftJoin(Positions)
            .leftJoin(Notes)
            .innerJoin(Users, { Employees.id }, { Users.id })
            .join(Contacts, JoinType.LEFT, Employees.id, Contacts.user)
            .select {
                Employees.id eq userId
            }.toEmployeeDetailsJoined().single()
}

fun Iterable<ResultRow>.toEmployeeDetailsJoined(): List<EmployeeDetails> {
    return fold(mutableMapOf<UUID, EmployeeDetails>()) { map, resultRow ->
        val details = resultRow.toEmployeeDetails()
        val current = map.getOrDefault(details.userId, details)

        val noteId = resultRow.getOrNull(Notes.id)
        val salaryId = resultRow.getOrNull(Salaries.id)
        val positionId = resultRow.getOrNull(Positions.id)
        val contactId = resultRow.getOrNull(Contacts.id)
        val notes = noteId?.let { resultRow.toNote() }
        val salaries = salaryId?.let { resultRow.toSalary() }
        val positions = positionId?.let { resultRow.toPosition() }
        val contacts = contactId?.let { resultRow.toContact() }

        map[details.userId] = current.copy(
            notes = current.notes.plus(listOfNotNull(notes)).distinct(),
            salaries = current.salaries.plus(listOfNotNull(salaries)).distinct(),
            positions = current.positions.plus(listOfNotNull(positions)).distinct(),
            contacts = current.contacts.plus(listOfNotNull(contacts)).distinct()
        )
        map
    }.values.toList()
}

fun ResultRow.toEmployeeDetails() = EmployeeDetails(
    this[Employees.id].value,
    this[Users.name[NameTable.firstName]],
    this[Users.name[NameTable.lastName]],
    this[Employees.skills],
    this[Employees.personalData[PersonalDataTable.birthday]],
    this[Employees.personalData[PersonalDataTable.bio]]
)

fun ResultRow.toNote() = Note(
    this[Notes.id].value,
    this[Notes.text],
    this[Notes.archived],
    this[Notes.hrManager],
    this[Notes.createdAt]
)

fun ResultRow.toSalary() = Salary(
    this[Salaries.id].value,
    this[Salaries.amount],
    this[Salaries.comment],
    this[Salaries.since],
    this[Salaries.till]
)

fun ResultRow.toPosition() = Position(
    this[Positions.id].value,
    this[Positions.title],
    this[Positions.since],
    this[Positions.till]
)

fun ResultRow.toContact() = Contact(
    this[Contacts.type].toString(),
    this[Contacts.value]
)

@Serializable
data class EmployeeDetails(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val firstName: String?,
    val lastName: String?,
    val skills: List<String>?,
    @Serializable(with = LocalDateSerializer::class)
    val birthday: LocalDate?,
    val bio: String?,
    var notes: List<Note> = listOf(),
    var salaries: List<Salary> = listOf(),
    var positions: List<Position> = listOf(),
    var contacts: List<Contact> = listOf()
)

@Serializable
data class Note(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val text: String,
    val archived: Boolean,
    @Serializable(with = UUIDSerializer::class)
    val authorId: UUID,
    @Serializable(with = LocalDateTimeSerializer::class) val createdAt: LocalDateTime?
)

@Serializable
data class Position(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val title: String,
    @Serializable(with = LocalDateSerializer::class) val since: LocalDate?,
    @Serializable(with = LocalDateSerializer::class) val till: LocalDate?
)

@Serializable
data class Salary(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val amount: Int,
    val comment: String,
    @Serializable(with = LocalDateSerializer::class) val since: LocalDate?,
    @Serializable(with = LocalDateSerializer::class) val till: LocalDate?
)

@Serializable
data class Contact(
    val type: String,
    val value: String
)
