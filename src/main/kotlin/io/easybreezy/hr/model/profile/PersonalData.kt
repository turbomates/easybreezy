package io.easybreezy.hr.model.profile

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.EmbeddableTable
import io.easybreezy.infrastructure.postgresql.PGEnum

class PersonalData private constructor() : Embeddable() {
    var birthday by PersonalDataTable.birthday
    var gender by PersonalDataTable.gender
    var about by PersonalDataTable.about
    var workStack by PersonalDataTable.workStack
    var name by PersonalDataTable.name

    companion object : EmbeddableClass<PersonalData>(PersonalData::class) {
        override fun createInstance(): PersonalData {
            return PersonalData()
        }

        fun create(name: Name): PersonalData {
            val personalData = PersonalData()
            personalData.name = name

            return personalData
        }
    }

    class Name private constructor() : Embeddable() {
        private var firstName by NameTable.firstName
        private var lastName by NameTable.lastName

        companion object : EmbeddableClass<Name>(Name::class) {
            override fun createInstance(): Name {
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

object PersonalDataTable : EmbeddableTable() {
    val birthday = date("birthday").nullable()
    val gender = customEnumeration(
        "gender",
        "profile_gender",
        { value -> Gender.valueOf(value as String) },
        { PGEnum("profile_gender", it) }).nullable()
    val about = text("about").nullable()
    val name = embedded<PersonalData.Name>(NameTable)
    val workStack = text("work_stack").nullable()
    enum class Gender {
        MALE, FEMALE
    }
}

object NameTable : EmbeddableTable() {
    val firstName = varchar("first_name", 25).nullable()
    val lastName = varchar("last_name", 25).nullable()
}

