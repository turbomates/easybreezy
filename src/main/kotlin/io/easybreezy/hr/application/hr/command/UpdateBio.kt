package io.easybreezy.hr.application.hr.command

import kotlinx.serialization.Serializable

@Serializable
data class UpdateBio (val bio: String)