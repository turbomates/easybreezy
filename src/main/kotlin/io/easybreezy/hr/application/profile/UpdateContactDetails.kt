package io.easybreezy.hr.application.profile

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class UpdateContactDetails(
    val messengers: Set<Map<String, String>>,
    val phones: Set<String>
) {
    @Transient
    lateinit var id: UUID
}
// enum class Messenger {
//     SKYPE
// }
// data class Messenger(val name: String, val username: String)

