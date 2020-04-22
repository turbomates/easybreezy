package io.easybreezy.infrastructure.ktor.auth

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.application.feature
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.RouteSelector
import io.ktor.routing.RouteSelectorEvaluation
import io.ktor.routing.RoutingResolveContext
import io.ktor.routing.application
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelinePhase
import org.slf4j.LoggerFactory

class Authorization(private var config: Configuration) {
    constructor() : this(Configuration())

    private val logger = LoggerFactory.getLogger(Authorization::class.java)
    private val rules: MutableMap<String, List<String>> = mutableMapOf()

    class Configuration() {
        internal var validate: suspend ApplicationCall.(Set<Activity>) -> Boolean = { false }
        internal var challenge: suspend suspend io.ktor.util.pipeline.PipelineContext<*, ApplicationCall>.() -> Unit =
            { call.respond(HttpStatusCode.Forbidden) }

        fun validate(block: suspend ApplicationCall.(Set<Activity>) -> Boolean) {
            this.validate = block
        }

        fun challenge(block: suspend io.ktor.util.pipeline.PipelineContext<*, ApplicationCall>.() -> Unit) {
            this.challenge = block
        }
    }

    fun rules(): Map<String, List<String>> {
        return this.rules
    }

    /**
     * Configures [pipeline] to process authentication by one or multiple auth methods
     * @param pipeline to be configured
     * @param configurationNames references to auth providers, could contain null to point to default
     */
    fun interceptPipeline(
        pipeline: ApplicationCallPipeline,
        activities: Set<Activity>
    ) {
        require(activities.isNotEmpty()) { "At least one activity should be in set" }
        rules[pipeline.toString()] = activities.map { it.name }
        pipeline.insertPhaseBefore(ApplicationCallPipeline.Call, AuthorizationCheckPhase)
        pipeline.intercept(AuthorizationCheckPhase) {
            if (!config.validate(call, activities)) {
                logger.debug("Unauthorized for activities: " + activities.joinToString(","))
                config.challenge(this)
                finish()
            }
        }
    }

    companion object Feature : ApplicationFeature<Application, Configuration, Authorization> {
        val AuthorizationCheckPhase: PipelinePhase = PipelinePhase("CheckAuthorize")
        override val key: AttributeKey<Authorization> = AttributeKey("Authorization")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): Authorization {
            return Authorization().apply {
                configure(configure)
            }
        }
    }

    /**
     * Configure already installed feature
     */
    fun configure(block: Configuration.() -> Unit) {
        block(config)
    }
}

fun Route.authorize(activities: Set<Activity>, build: Route.() -> Unit): Route {
    val authenticatedRoute = createChild(AuthorizationRouteSelector(activities))
    application.feature(Authorization).interceptPipeline(authenticatedRoute, activities)
    authenticatedRoute.build()
    return authenticatedRoute
}

class AuthorizationRouteSelector(private val activities: Set<Activity>) :
    RouteSelector(RouteSelectorEvaluation.qualityConstant) {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
        return RouteSelectorEvaluation.Constant
    }

    override fun toString(): String = "(authorize ${activities.joinToString { it.name }})"
}
