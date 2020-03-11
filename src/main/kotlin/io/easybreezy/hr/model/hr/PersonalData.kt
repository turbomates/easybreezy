package io.easybreezy.hr.model.hr

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.EmbeddableTable
import java.time.LocalDate

class PersonalData private constructor() : Embeddable() {
    var birthday by PersonalDataTable.birthday
    var bio by PersonalDataTable.bio

    companion object : EmbeddableClass<PersonalData>(PersonalData::class) {
        override fun createInstance(): PersonalData {
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

object PersonalDataTable : EmbeddableTable() {
    val birthday = date("birthday").nullable()
    val bio = text("bio").nullable()
}
