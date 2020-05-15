package io.easybreezy.integration.gitlab.webhook

import io.easybreezy.integration.gitlab.webhook.action.Issue
import io.easybreezy.integration.gitlab.webhook.action.Job
import io.easybreezy.integration.gitlab.webhook.action.MergeRequest
import io.easybreezy.integration.gitlab.webhook.action.Note
import io.easybreezy.integration.gitlab.webhook.action.ObjectKind
import io.easybreezy.integration.gitlab.webhook.action.Pipeline
import io.easybreezy.integration.gitlab.webhook.action.Push
import io.easybreezy.integration.gitlab.webhook.action.WikiPage
import io.easybreezy.integration.inner.Sender
import io.easybreezy.integration.inner.message.Author
import io.easybreezy.integration.inner.message.Project
import io.easybreezy.integration.inner.message.Reference
import io.easybreezy.integration.inner.message.ReviewComment
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer

class WebHook(private val token: String, private val sender: Sender) {
    val json = Json(JsonConfiguration.Stable)
    fun validate(key: String) {
        if (key != this.token) {
            throw InvalidWebHookToken()
        }
    }

    fun getKind(request: String): ObjectKind {
        val jsonElement = json.parseJson(request) as? JsonObject
        val kind = jsonElement!!["object_kind"]?.primitive?.content?.let { ObjectKind.valueOf(it) }
            ?: throw InvalidWebHookKind()
        return kind!!
    }

    @ImplicitReflectionSerializer
    inline fun <reified T : Any> get(text: String): T {
        return json.parse(T::class.serializer(), text)
    }

    private fun note(note: Note) {
        val comment = when (note.objectAttributes.noteableType) {
            "MergeRequest" -> {
                ReviewComment(
                    "sdf",
                    note.objectAttributes.note,
                    note.objectAttributes.url,
                    Author(note.user.name),
                    Reference(
                        "Merge Request",
                        Author("dl"),
                        note.mergeRequest!!.title,
                        note.mergeRequest!!.state
                    ),
                    Project("s", "s", "d")
                )
            }
            "Commit" -> {
//                val comment = ReviewComment("")
            }
            "Issue" -> {
//                val comment = IssueComment("")
            }
            "Snippet" -> {
            }
            else -> {
            }
        }
    }

    private fun pipeline(pipeline: Pipeline) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun issue(issue: Issue) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun mergeRequest(mr: MergeRequest) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun job(job: Job) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun wikiPage(wiki: WikiPage) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun push(push: Push) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
