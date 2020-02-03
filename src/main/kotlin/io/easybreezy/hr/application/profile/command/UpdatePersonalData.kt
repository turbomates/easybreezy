package io.easybreezy.hr.application.profile.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class UpdatePersonalData(
    val birthday: String,
    val gender: String,
    val about: String,
    val workStack: String,
    val firstName: String,
    val lastName: String
) {
    @Transient
    lateinit var id: UUID
}
