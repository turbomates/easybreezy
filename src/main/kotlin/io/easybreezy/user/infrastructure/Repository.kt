package io.easybreezy.user.infrastructure

import com.google.inject.Inject
import io.easybreezy.user.model.User
import io.easybreezy.user.model.UserId
import org.hibernate.Session
import io.easybreezy.user.model.Repository as RepositoryInterface

class Repository @Inject constructor(private val session: Session) : RepositoryInterface {
    override fun addUser(user: User) {
        session.persist(user)
    }

    override fun findUser(id: UserId): User {
        return session.find(User::class.java, id)
    }
}
