package io.easybreezy.project

import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Provides
import com.google.inject.multibindings.Multibinder
import io.easybreezy.infrastructure.event.EventSubscribers
import io.easybreezy.infrastructure.event.GuiceSubscriberFactory
import io.easybreezy.infrastructure.ktor.ControllerPipeline
import io.easybreezy.infrastructure.ktor.ControllerPipelineFactory
import io.easybreezy.infrastructure.ktor.InterceptorPipeline
import io.easybreezy.infrastructure.ktor.InterceptorPipelineFactory
import io.easybreezy.project.api.Router
import io.easybreezy.project.application.issue.command.Handler
import io.easybreezy.project.application.issue.command.language.normalizer.LabelNormalizer
import io.easybreezy.project.application.issue.command.language.normalizer.CategoryNormalizer
import io.easybreezy.project.application.issue.command.language.normalizer.PriorityNormalizer
import io.easybreezy.project.application.issue.command.language.normalizer.ParticipantsNormalize
import io.easybreezy.project.application.issue.command.language.normalizer.ElementNormalizer
import io.easybreezy.project.application.issue.subscriber.IssueNumberSubscriber
import io.easybreezy.project.application.issue.subscriber.IssueLabelsSubscriber
import io.easybreezy.project.application.issue.subscriber.IssueParticipantsSubscriber
import io.easybreezy.project.application.issue.subscriber.IssueTimingSubscriber
import io.easybreezy.project.application.issue.subscriber.IssueWorkflowSubscriber
import io.easybreezy.project.infrastructure.ProjectRepository
import io.easybreezy.project.infrastructure.TeamRepository
import io.easybreezy.project.model.Repository

class ProjectModule : AbstractModule() {
    override fun configure() {
        bind(SubscriberDescription::class.java).asEagerSingleton()
        bind(Router::class.java).asEagerSingleton()
        bind(Repository::class.java).to(ProjectRepository::class.java)
        bind(io.easybreezy.project.model.team.Repository::class.java).to(TeamRepository::class.java)

        val normalizers: Multibinder<ElementNormalizer> = Multibinder.newSetBinder(binder(), ElementNormalizer::class.java)
        normalizers.addBinding().to(PriorityNormalizer::class.java)
        normalizers.addBinding().to(ParticipantsNormalize::class.java)
        normalizers.addBinding().to(CategoryNormalizer::class.java)
        normalizers.addBinding().to(LabelNormalizer::class.java)
    }

    @Provides
    fun controllerFactory(injector: Injector): ControllerPipeline {
        return ControllerPipelineFactory(injector)
    }

    @Provides
    fun interceptorFactory(injector: Injector): InterceptorPipeline {
        return InterceptorPipelineFactory(injector)
    }

    @Provides
    fun subscriberFactory(injector: Injector): GuiceSubscriberFactory {
        return GuiceSubscriberFactory(injector)
    }
}

class SubscriberDescription @Inject constructor(eventSystem: EventSubscribers, handler: Handler) {
    init {
        eventSystem.subscribe(IssueParticipantsSubscriber(handler))
        eventSystem.subscribe(IssueTimingSubscriber(handler))
        eventSystem.subscribe(IssueWorkflowSubscriber(handler))
        eventSystem.subscribe(IssueLabelsSubscriber(handler))
        eventSystem.subscribe(IssueNumberSubscriber(handler))
    }
}
