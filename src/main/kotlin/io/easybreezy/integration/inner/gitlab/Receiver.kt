package io.easybreezy.integration.inner.gitlab

import io.easybreezy.integration.gitlab.webhook.WebHook
import io.easybreezy.integration.gitlab.webhook.action.Issue
import io.easybreezy.integration.gitlab.webhook.action.Job
import io.easybreezy.integration.gitlab.webhook.action.MergeRequest
import io.easybreezy.integration.gitlab.webhook.action.Note
import io.easybreezy.integration.gitlab.webhook.action.ObjectKind
import io.easybreezy.integration.gitlab.webhook.action.Pipeline
import io.easybreezy.integration.gitlab.webhook.action.Push
import io.easybreezy.integration.gitlab.webhook.action.WikiPage
import io.easybreezy.integration.inner.LocalInformation
import io.easybreezy.integration.inner.MessageHandler
import kotlinx.serialization.ImplicitReflectionSerializer

class Receiver(private val webHook: WebHook, private val handler: MessageHandler, private val project: LocalInformation) {
    @ImplicitReflectionSerializer
    fun receive(text: String, key: String) {
        webHook.validate(key)
        when (webHook.getKind(text)) {
            ObjectKind.PUSH -> push(webHook.get(text))
            ObjectKind.PUSH_TAG -> push(webHook.get(text))
            ObjectKind.WIKI_PAGE -> wikiPage(webHook.get(text))
            ObjectKind.JOB -> job(webHook.get(text))
            ObjectKind.MERGE_REQUEST -> mergeRequest(webHook.get(text))
            ObjectKind.ISSUE -> issue(webHook.get(text))
            ObjectKind.PIPELINE -> pipeline(webHook.get(text))
            ObjectKind.NOTE -> note(webHook.get(text))
        }
    }

    private fun note(note: Note) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun pipeline(pipeline: Pipeline) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun issue(issue: Issue) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun mergeRequest(mergeRequest: MergeRequest) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun job(job: Job) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun wikiPage(wikiPage: WikiPage) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun push(push: Push) {
    }
}
