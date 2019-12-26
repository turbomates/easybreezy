package io.easybreezy.integration.inner

import io.easybreezy.integration.inner.message.CI
import io.easybreezy.integration.inner.message.Comment
import io.easybreezy.integration.inner.message.Issue
import io.easybreezy.integration.inner.message.VSC

interface Sender {
    fun sendVCSEvent(vsc: VSC)
    fun sendCIEvent(ci: CI)
    fun sendComment(comment: Comment)
    fun sendIssueEvent(issue: Issue)
}