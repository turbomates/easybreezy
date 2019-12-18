package io.easybreezy.integration.gitlab.webhook.action

import java.time.LocalDate

data class Changes(
    val updatedById: List<String?>,
    val updatedAt: List<LocalDate>,
    val labels: Map<String, Label>
)