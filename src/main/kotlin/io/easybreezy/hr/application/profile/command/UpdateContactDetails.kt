package io.easybreezy.hr.application.profile.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class UpdateContactDetails(
    val emails: Set<String>,
    val phones: Set<String>
) {
    @Transient
    lateinit var id: UUID
}
