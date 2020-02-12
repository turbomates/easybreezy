package io.easybreezy.hr.application.hr.command

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CreateCard(
    val firstName: String,
    val lastName: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthday: LocalDate? = null,
    val bio: String? = null
)