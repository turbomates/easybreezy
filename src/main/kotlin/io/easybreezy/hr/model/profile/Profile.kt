package io.easybreezy.hr.model.profile

import io.easybreezy.infrastructure.event.profile.MessengerAdded
import io.easybreezy.infrastructure.event.profile.MessengerRemoved
import io.easybreezy.infrastructure.event.profile.MessengerUsernameChanged
import io.easybreezy.infrastructure.exposed.dao.*
// import io.easybreezy.infrastructure.exposed.dao.EmbeddableColumn
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.infrastructure.postgresql.PGEnum
import kotlinx.serialization.Serializable
import kotlinx.serialization.set
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate
import java.util.UUID

class Profile private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var personalData by Embedded(PersonalData)
    private var contactDetails by Embedded(ContactDetails)
    private var userId by Profiles.userId
    private val messengers by Messenger referrersOn Messengers.profile

    fun addMessenger(type: String, username: String) {
        if (hasMessenger(type)) throw Exception("Messenger with $type already exist")
        Messenger.create(this, Messengers.Type.valueOf(type.toUpperCase()), username)
        this.addEvent(MessengerAdded(id.value, type))
    }

    fun hasMessenger(type: String): Boolean = messengers.any {
        it.type == Messengers.Type.valueOf(type.toUpperCase()) && it.profile == this
    }

    fun removeMessenger(messenger: Messenger) {
        messenger.delete()
        this.addEvent(MessengerRemoved(id.value, messenger.type.name))
    }

    fun renameMessengerUsername(type: String, username: String) {
        if (!hasMessenger(type)) throw Exception("Messenger with $type doesn't exist")
        val messenger =
            messengers.first { it.type == Messengers.Type.valueOf(type.toUpperCase()) && it.profile == this }
        messenger.changeUsername(username)
        this.addEvent(MessengerUsernameChanged(id.value, type))
    }

    fun messengers() = messengers

    fun updatePersonalData(personalData: PersonalData) {
        this.personalData = personalData
    }

    fun updateContactDetails(contactDetails: ContactDetails) {
        this.contactDetails = contactDetails
    }

    companion object : PrivateEntityClass<UUID, Profile>(object : Repository() {}) {
        fun create(userId: UUID, personalData: PersonalData) = Profile.new {
            this.userId = userId
            this.personalData = personalData
        }
    }

    abstract class Repository : EntityClass<UUID, Profile>(
        Profiles, Profile::class.java
    ) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Profile {
            return Profile(entityId)
        }
    }
}

object Profiles : UUIDTable() {
    val userId = uuid("user_id")
    val birthday = date("birthday").nullable()
    val gender = customEnumeration(
        "gender",
        "profile_gender",
        { value -> Gender.valueOf(value as String) },
        { PGEnum("profile_gender", it) }).nullable()
    val about = text("about").nullable()
    val firstName = varchar("first_name", 25).nullable()
    val lastName = varchar("last_name", 25).nullable()
    val phones = jsonb("phones", Phone.serializer().set).nullable()
    val emails = jsonb("emails", Email.serializer().set).nullable()

    enum class Gender {
        MALE, FEMALE
    }

    @Serializable
    class Phone(val number: String)

    @Serializable
    class Email(val address: String)
}
