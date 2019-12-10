package io.easybreezy.infrastructure.ktor

import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

abstract class Controller {
    lateinit var pipeline: PipelineContext<*, ApplicationCall>
    val call: ApplicationCall get() = pipeline.context
}
