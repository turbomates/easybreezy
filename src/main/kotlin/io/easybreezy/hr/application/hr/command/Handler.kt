package io.easybreezy.hr.application.hr.command

import com.google.inject.Inject
import io.easybreezy.hr.model.hr.Employee
import io.easybreezy.hr.model.hr.PersonalData
import io.easybreezy.hr.model.hr.Repository
import io.easybreezy.infrastructure.exposed.TransactionManager
import java.util.*

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val repository: Repository
) {

    suspend fun createCard(command: CreateCard, userId: UUID) {
        transaction {
            Employee.createCard(
                userId,
                PersonalData.create(
                    command.birthday,
                    command.bio
                )
            )
        }
    }

    suspend fun hire(command: Hire, userId: UUID, hrManager: UUID) {
        transaction {

            val employee = employee(userId)
            employee.applyPosition(hrManager, command.position, command.hiredAt)
            employee.applySalary(hrManager, command.salary, "", command.hiredAt)

            command.bio?.let { employee.updateBio(it) }
            command.birthday?.let { employee.updateBirthday(it) }
            command.skills?.let {employee.specifySkills(it)}
        }
    }

    suspend fun fire(command: Fire, employee: UUID, hrManager: UUID) {
        transaction {
            employee(employee).fire(hrManager, command.comment, command.firedAt)
        }
    }

    suspend fun writeNote(command: WriteNote, employee: UUID, hrManager: UUID) {
        transaction {
            employee(employee).note(hrManager, command.text)
        }
    }

    suspend fun applyPosition(command: ApplyPosition, employee: UUID, hrManager: UUID) {
        transaction {
            employee(employee).applyPosition(hrManager, command.position, command.appliedAt)
        }
    }

    suspend fun applySalary(command: ApplySalary, employee: UUID, hrManager: UUID) {
        transaction {
            employee(employee).applySalary(hrManager, command.amount, command.comment, command.appliedAt)
        }
    }

    suspend fun specifySkills(command: SpecifySkills, employee: UUID) {
        transaction {
            employee(employee).specifySkills(command.skills)
        }
    }

    suspend fun updateBio(command: UpdateBio, employee: UUID) {
        transaction {
            employee(employee).updateBio(command.bio)
        }
    }

    suspend fun updateBirthday(command: UpdateBirthday, employee: UUID) {
        transaction {
            employee(employee).updateBirthday(command.birthday)
        }
    }

    private fun employee(userId: UUID) = repository.getByUserId(userId)
}