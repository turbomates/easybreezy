package io.easybreezy.hr.model.hr

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDate

class PersonalData private constructor() : Embeddable() {
    var birthday by Employees.birthday
    var bio by Employees.bio

    companion object : EmbeddableClass<PersonalData>(PersonalData::class) {
        override fun createInstance(resultRow: ResultRow?): PersonalData {
            return PersonalData()
        }

        fun create(birthday: LocalDate? = null, bio: String? = null): PersonalData {
            val personalData = PersonalData()
            personalData.birthday = birthday
            personalData.bio = bio

            return personalData
        }
    }
}
