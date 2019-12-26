package io.easybreezy.project.model.project

import io.easybreezy.project.model.team.Member
import java.util.*

class Comment(
    private val issue: UUID,
    private val body: String,
    private val author: Member
) {
    private val id: UUID = UUID.randomUUID()
    private val members: List<Member> = emptyList()
}