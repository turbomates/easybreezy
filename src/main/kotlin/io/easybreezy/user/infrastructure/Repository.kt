package io.easybreezy.user.infrastructure

import com.google.inject.Inject
import io.easybreezy.user.model.User
import io.easybreezy.user.model.UserId
import org.hibernate.Session
import java.util.Optional
import io.easybreezy.user.model.Repository as RepositoryInterface

class Repository @Inject constructor(private val session: Session) : RepositoryInterface {
    override fun addUser(user: User) {
        session.persist(user)
    }

    override fun findUser(id: UserId): User {
        return session.find(User::class.java, id)
    }

    override fun findByToken(token: String): Optional<User> {
        val criteriaQuery = session.criteriaBuilder.createQuery(User::class.java)
        val root = criteriaQuery.from(User::class.java)
        val cb = session.criteriaBuilder
        criteriaQuery
            .where(
                cb.equal(root.get<String>("token"), token)
            )

        val query = session.createQuery(criteriaQuery)

        return query.uniqueResultOptional()
    }
}
