package io.easybreezy.user.infrastructure

import io.easybreezy.user.model_legacy.User
import io.easybreezy.user.model_legacy.UserId
import java.util.Optional
import io.easybreezy.user.model_legacy.Repository as RepositoryInterface

class Repository : RepositoryInterface {
    override fun addUser(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findUser(id: UserId): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByToken(token: String): Optional<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    // override fun addUser(user: User) {
    //     session.persist(user)
    // }
    //
    // override fun findUser(id: UserId): User {
    //     return session.find(User::class.java, id)
    // }
    //
    // override fun findByToken(token: String): Optional<User> {
    //     val criteriaQuery = session.criteriaBuilder.createQuery(User::class.java)
    //     val root = criteriaQuery.from(User::class.java)
    //     val cb = session.criteriaBuilder
    //     criteriaQuery
    //         .where(
    //             cb.equal(root.get<String>("token"), token)
    //         )
    //
    //     val query = session.createQuery(criteriaQuery)
    //
    //     return query.uniqueResultOptional()
    // }
}
