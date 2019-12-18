package io.easybreezy.integration.gitlab.webhook.action

import java.time.LocalDate

data class Pipline(
    val objectKind: ObjectKind,
    val objectAttributes: ObjectAttributes,
    val user: User,
    val project: Project,
    val commit: Commit,
    val jobs: List<Job>
) {
    data class ObjectAttributes(
        val id: Int,
        val ref: String,
        val tag: Boolean,
        val sha: String,
        val beforeSha: String,
        val status: String,
        val stages: List<String>,
        val createdAt: LocalDate,
        val updatedAt: LocalDate,
        val duration: Int,
        val variables: List<Map<String, String>>
    )

    data class Job(
        val id: Int,
        val stage: String,
        val name: String,
        val status: String,
        val startedAt: LocalDate,
        val createdAt: LocalDate,
        val finishedAt: LocalDate,
        val whenStart: String, //when
        val manual: Boolean,
        val user: User,
        val runner: Runner?,
        val artifactsFile: Map<String, String>
    ) {
        data class Runner(
            val id: Int,
            val description: String,
            val active: Boolean,
            val isShared: Boolean

        )
    }
}