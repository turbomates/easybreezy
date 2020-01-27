package io.easybreezy.integration.inner.message

sealed class VSC()

data class PullRequest(val name: String) : VSC()
data class Commit(val name: String) : VSC()
