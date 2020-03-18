package io.easybreezy.hr.application.hr.command

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class UpdateBirthday(
    @Serializable(with = LocalDateSerializer::class)
    val birthday: LocalDate
)
