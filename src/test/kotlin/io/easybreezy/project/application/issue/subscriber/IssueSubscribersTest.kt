package io.easybreezy.project.application.issue.subscriber

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.multibindings.Multibinder
import io.easybreezy.createIssue
import io.easybreezy.createMember
import io.easybreezy.createMyProject
import io.easybreezy.createProjectRole
import io.easybreezy.createProjectTeam
import io.easybreezy.createTeamMember
import io.easybreezy.infrastructure.event.EventSubscribers
import io.easybreezy.infrastructure.event.project.issue.Created
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.issue.command.language.normalizer.ElementNormalizer
import io.easybreezy.project.application.issue.command.language.normalizer.LabelNormalizer
import io.easybreezy.project.application.issue.command.language.normalizer.ParticipantsNormalizer
import io.easybreezy.project.application.issue.queryobject.IssueParticipantsQO
import io.easybreezy.project.application.issue.queryobject.IssueQO
import io.easybreezy.project.application.issue.queryobject.IssueTimingQO
import io.easybreezy.project.infrastructure.ProjectRepository
import io.easybreezy.project.model.Repository
import io.easybreezy.rollbackTransaction
import io.easybreezy.testDatabase
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class IssueSubscribersTest {

    companion object {
        var injector: Injector = Guice.createInjector(object : AbstractModule() {
            override fun configure() {
                bind(TransactionManager::class.java).toInstance(TransactionManager(testDatabase))
                bind(Repository::class.java).to(ProjectRepository::class.java)

                val normalizers: Multibinder<ElementNormalizer> = Multibinder.newSetBinder(binder(), ElementNormalizer::class.java)
                normalizers.addBinding().to(ParticipantsNormalizer::class.java)
                normalizers.addBinding().to(LabelNormalizer::class.java)
            }
        })
    }

    private val eventSubscribers: EventSubscribers = EventSubscribers()
    private val labelsSubscriber: IssueLabelsSubscriber = injector.getInstance(IssueLabelsSubscriber::class.java)
    private val participantsSubscriber: IssueParticipantsSubscriber = injector.getInstance(IssueParticipantsSubscriber::class.java)
    private val timingSubscriber: IssueTimingSubscriber = injector.getInstance(IssueTimingSubscriber::class.java)
    private val queryExecutor: QueryExecutor = injector.getInstance(QueryExecutor::class.java)

    @Test fun `assign labels`() {
        rollbackTransaction {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val issue = testDatabase.createIssue(project.value)
            val event = Created(project.value, issue.value, userId, "title", "*label1* *label2*", LocalDateTime.now())

            eventSubscribers.subscribe(labelsSubscriber)
            runBlocking {
                eventSubscribers.call(event)
                val details = queryExecutor.execute(IssueQO(issue.value))
                assertEquals(2, details.labels.count())
            }
        }
    }

    @Test fun `create participants`() {
        rollbackTransaction {
            val userId = testDatabase.createMember()
            val john = testDatabase.createMember(username = "john", email = "john@gmail.com")
            val bob = testDatabase.createMember(username = "bob", email = "bob@gmail.com")
            val project = testDatabase.createMyProject()
            val role = testDatabase.createProjectRole(project, "role")
            val team = testDatabase.createProjectTeam(project.value, "team")
            testDatabase.createTeamMember(team, john, role)
            testDatabase.createTeamMember(team, bob, role)
            val issue = testDatabase.createIssue(project.value)
            val event = Created(project.value, issue.value, userId, "title", "let @john and @bob", LocalDateTime.now())

            eventSubscribers.subscribe(participantsSubscriber)
            runBlocking {
                eventSubscribers.call(event)
                val details = queryExecutor.execute(IssueParticipantsQO(issue.value))
                assertEquals(john, details.assignee)
                assertEquals(listOf(john, bob), details.watchers)
            }
        }
    }

    @Test fun `create timing`() {
        rollbackTransaction {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val issue = testDatabase.createIssue(project.value)
            val event = Created(project.value, issue.value, userId, "title", "due is 01/01/2020", LocalDateTime.now())

            eventSubscribers.subscribe(timingSubscriber)
            runBlocking {
                eventSubscribers.call(event)
                val details = queryExecutor.execute(IssueTimingQO(issue.value))
                assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0), details.dueDate)
            }
        }
    }
}
