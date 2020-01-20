package io.easybreezy.hr.application.profile

import java.util.UUID

data class UpdateContactDetails(
    var id: UUID,
    val messengers: Set<Map<Messenger, String>>,
    val phones: Set<String>
)
enum class Messenger {
    SKYPE
}
// data class Messenger(val name: String, val username: String)

