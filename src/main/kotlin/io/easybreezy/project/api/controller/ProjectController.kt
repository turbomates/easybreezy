package io.easybreezy.project.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.respondOk
import io.easybreezy.project.application.project.command.Handler
import io.easybreezy.project.application.project.command.New

class ProjectController @Inject constructor(private val handler: Handler) : Controller() {
    suspend fun create(new: New) {
        handler.new(new)
        call.respondOk()
    }
}
