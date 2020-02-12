package io.easybreezy.hr.model.profile

import io.easybreezy.infrastructure.event.profile.MessengerAdded
import io.easybreezy.infrastructure.event.profile.MessengerRemoved
import io.easybreezy.infrastructure.event.profile.MessengerUsernameChanged
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.Embedded
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.type.jsonb
import kotlinx.serialization.Serializable
import kotlinx.serialization.set
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

class Profile private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {

    private var contactDetails by Embedded(ContactDetails)
    private var userId by Profiles.userId
    private val messengers by Messenger.referrersOn(Messengers.profile, true)

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

    fun updateContactDetails(contactDetails: ContactDetails) {
        this.contactDetails = contactDetails
    }

    companion object : PrivateEntityClass<UUID, Profile>(object : Repository() {}) {
        fun create(userId: UUID) = Profile.new {
            this.userId = userId
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

    val phones = jsonb("phones", Phone.serializer().set).nullable()
    val emails = jsonb("emails", Email.serializer().set).nullable()

    @Serializable
    class Phone(val number: String)

    @Serializable
    class Email(val address: String)
}
