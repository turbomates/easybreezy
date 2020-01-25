package io.easybreezy.hr.model.profile

import io.easybreezy.infrastructure.exposed.dao.*
//import io.easybreezy.infrastructure.exposed.dao.EmbeddableColumn
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.user.model.PGEnum
import kotlinx.serialization.Serializable
import kotlinx.serialization.set
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
// import org.jetbrains.exposed.sql.date
import java.time.LocalDate
import java.util.UUID

enum class Gender {
    MALE, FEMALE
}

@Serializable
class MessengerInfo(val messenger: Messenger, val username: String)

@Serializable
enum class Messenger {
    SLACK, TELEGRAM, TWITTER, SKYPE
}

@Serializable
class Phone(val number: String)

object Profiles : UUIDTable() {
    val userId = uuid("user_id")
    val gender = customEnumeration(
        "gender",
        "profile_gender",
        { value -> Gender.valueOf(value as String) },
        { PGEnum("profile_gender", it) }).nullable()
    val about = text("about").nullable()
    val firstName = varchar("first_name", 25).nullable()
    val lastName = varchar("last_name", 25).nullable()
    val messengers = jsonb("messengers", MessengerInfo.serializer().set)
    val phones = jsonb("phones", Phone.serializer().set)
}

class Profile private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var personalData by Embedded(PersonalData)
    private var contactDetails by Embedded(ContactDetails)

    class PersonalData private constructor() : Embeddable() {
        //        private var birthday by Profiles.birthday
        private var gender by Profiles.gender
        private var about by Profiles.about
        private var name by Embedded(Name)

        companion object : EmbeddableClass<PersonalData>(PersonalData::class) {
            override fun createInstance(resultRow: ResultRow?): PersonalData {
                return PersonalData()
            }

            fun create(birthday: LocalDate, gender: Gender, about: String): PersonalData {
                val data = PersonalData()
                // this.birthday = birthday
                data.gender = gender
                data.about = about
                return data
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

    class ContactDetails private constructor() : Embeddable() {
        private var messengers by Profiles.messengers
        private var phones by Profiles.phones

        companion object : EmbeddableClass<ContactDetails>(ContactDetails::class) {
            override fun createInstance(resultRow: ResultRow?): ContactDetails {
                return ContactDetails()
            }

            fun create(messengers: Set<MessengerInfo>, phones: Set<Phone>): ContactDetails {
                val details = ContactDetails()
                details.messengers = messengers
                details.phones = phones
                return details
            }
        }
    }

    companion object : PrivateEntityClass<UUID, Profile>(object : Repository() {}) {
    }

    fun updatePersonalData(personalData: PersonalData) {
        this.personalData = personalData
    }

    fun updateContactDetails(contactDetails: ContactDetails) {
        this.contactDetails = contactDetails
    }

    abstract class Repository : EntityClass<UUID, Profile>(
        Profiles, Profile::class.java
    ) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Profile {
            return Profile(entityId)
        }
    }
}
