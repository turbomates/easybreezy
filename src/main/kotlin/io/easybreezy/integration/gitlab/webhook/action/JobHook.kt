package io.easybreezy.integration.gitlab.webhook.action

import java.time.LocalDate

data class JobHook(
    val objectKind: ObjectKind,
    val ref: String,
    val tag: Boolean,
    val beforeSha: String,
    val sha: String,
    val jobId: Int,
    val jobName: String,
    val jobStage: String,
    val jobStatus: String,
    val jobStartedAt: LocalDate?,
    val jobFinishedAt: LocalDate?,
    val jobDuration: Int?,
    val jobAllowFailure: Boolean,
    val jobFailureReason: String?,
    val projectId: Int,
    val projectName: String,
    val user: User,
    val commit: Commit,
    val repository: Repository
)