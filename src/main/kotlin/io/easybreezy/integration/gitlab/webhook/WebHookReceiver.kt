package io.easybreezy.integration.gitlab.webhook

class WebHookReceiver(private val token: String) {
    val json = Json(JsonConfiguration.Stable)
    fun <T> applyAction(token: String, request: String): T {
        if (token != this.token) {
            throw InvalidWebHookToken()
        }

    }
}