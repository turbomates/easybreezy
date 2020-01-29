package io.easybreezy.hr.model.profile

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.Embedded
import org.jetbrains.exposed.sql.ResultRow

class PersonalData private constructor() : Embeddable() {
    var birthday by Profiles.birthday
    var gender by Profiles.gender
    var about by Profiles.about
    var name by Embedded(Name)

    companion object : EmbeddableClass<PersonalData>(PersonalData::class) {
        override fun createInstance(resultRow: ResultRow?): PersonalData {
            return PersonalData()
        }

        fun create(name: Name): PersonalData {
            val personalData = PersonalData()
            personalData.name = name

            return personalData
        }
    }

    class Name private constructor() : Embeddable() {
        private var firstName by Profiles.firstName
        private var lastName by Profiles.lastName

        companion object : EmbeddableClass<Name>(Name::class) {
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
