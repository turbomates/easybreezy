package io.easybreezy.integration.inner

import io.easybreezy.integration.inner.message.CI
import io.easybreezy.integration.inner.message.Comment
import io.easybreezy.integration.inner.message.Issue
import io.easybreezy.integration.inner.message.VSC

class Inner : Sender {
    override fun sendVCSEvent(vsc: VSC) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun sendCIEvent(ci: CI) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun sendComment(comment: Comment) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun sendIssueEvent(issue: Issue) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
