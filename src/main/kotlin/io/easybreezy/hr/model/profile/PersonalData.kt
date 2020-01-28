package io.easybreezy.hr.model.profile

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.Embedded
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDate

class PersonalData private constructor(builder: Builder) : Embeddable() {
    private var birthday by Profiles.birthday
    private var gender by Profiles.gender
    private var about by Profiles.about
    private var name by Embedded(Name)

    init {
        this.birthday = builder.birthday
        this.gender = builder.gender
        this.about = builder.about
        builder.name?.let {
            this.name = it
        }
    }

    companion object : EmbeddableClass<PersonalData>(PersonalData::class) {
        override fun createInstance(resultRow: ResultRow?): PersonalData {
            return create {}
        }

        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
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

    class Builder {
        var name: Name? = null
        var birthday: LocalDate? = null
        var gender: Profiles.Gender? = null
        var about: String? = null

        fun build() = PersonalData(this)
    }
}
