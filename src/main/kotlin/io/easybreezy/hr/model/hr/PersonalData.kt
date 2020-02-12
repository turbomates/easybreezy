package io.easybreezy.hr.model.hr

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.Embedded
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDate

class PersonalData private constructor() : Embeddable() {
    var birthday by Employees.birthday
    var bio by Employees.bio
    var name by Embedded(Name)

    companion object : EmbeddableClass<PersonalData>(PersonalData::class) {
        override fun createInstance(resultRow: ResultRow?): PersonalData {
            return PersonalData()
        }

        fun create(name: Name, birthday: LocalDate? = null, bio: String? = null): PersonalData {
            val personalData = PersonalData()
            personalData.name = name
            personalData.birthday = birthday
            personalData.bio = bio

            return personalData
        }
    }

    class Name private constructor() : Embeddable() {
        private var firstName by Employees.firstName
        private var lastName by Employees.lastName

        companion object : EmbeddableClass<Name>(
            Name::class) {
            override fun createInstance(resultRow: ResultRow?): Name {
                return Name()
            }

            fun create(firstName: String, lastName: String): Name {
                val name = Name()
                name.firstName = firstName
                name.lastName = lastName
                return name
            }
        }
    }
}
