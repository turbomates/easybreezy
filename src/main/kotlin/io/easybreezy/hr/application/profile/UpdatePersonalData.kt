package io.easybreezy.hr.application.profile

import java.util.UUID

class UpdatePersonalData(
    var id: UUID,
    val birthday: String,
    val gender: String,
    val about: String
)
