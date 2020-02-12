package io.easybreezy.hr.application.hr.command

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Hire (
    val position: String,
    val salary: Int,
    val bio: String? = null,
    @Serializable(with=LocalDateSerializer::class)
    val birthday: LocalDate? = null,
    val skills: List<String>? = null,
    @Serializable(with = LocalDateSerializer::class)
    val hiredAt: LocalDate = LocalDate.now()
)