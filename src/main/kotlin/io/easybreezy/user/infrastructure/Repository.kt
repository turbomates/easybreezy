package io.easybreezy.user.infrastructure

import com.google.inject.Inject
import io.easybreezy.user.model.Email
import io.easybreezy.user.model.Name
import io.easybreezy.user.model.User
import io.easybreezy.user.model.UserGateway
import io.easybreezy.user.model.Users
import io.easybreezy.user.model_legacy.UserId
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import io.easybreezy.user.model.Repository as RepositoryInterface

class Repository @Inject constructor() : RepositoryInterface {
    override fun addUser(user: User) {
      //  gateway.save(user)

        // Users.insert {
        //     it[id] = user.id()
        //     it[email] = user.email()
        //     it[status] = user.status()
        //     it[roles] = user.roles()
        // }
    }

    // fun findUser(id: UserId): User? =
    //     Users.select { Users.id eq id }
    //         .map { it.toUser() }
    //         .firstOrNull()

    // private fun ResultRow.toUser() = User(
    //     id = this[Users.id],
    //     email = Email(this[Users.email]),
    //     roles = this[Users.roles],
    //     status = this[Users.status],
    //     token = this[Users.token],
    //     name = Name(this[Users.firstName], this[Users.lastName])
    // )

    // override fun findUser(id: UserId): User {
    //     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    // }
    //
    // override fun findByToken(token: String): Optional<User> {
    //     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    // }
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
