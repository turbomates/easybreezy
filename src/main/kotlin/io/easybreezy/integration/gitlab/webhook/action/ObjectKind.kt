package io.easybreezy.integration.gitlab.webhook.action

enum class ObjectKind {
    PUSH,
    PUSH_TAG,
    ISSUE,
    NOTE,
    MERGE_REQUEST,
    WIKI_PAGE,
    PIPELINE,
    JOB
}
