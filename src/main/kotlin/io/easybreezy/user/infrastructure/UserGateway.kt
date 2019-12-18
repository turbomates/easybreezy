package io.easybreezy.user.infrastructure

import io.easybreezy.user.model.User
import io.easybreezy.user.model.UserEntity
import io.easybreezy.user.model.Users
import org.jetbrains.exposed.sql.insert
import java.util.UUID
import io.easybreezy.user.model.UserGateway as UserGatewayInterface

class UserGateway : UserGatewayInterface {
    override fun save(user: User) {
        // UserEntity.new {
        //     email = user.email()
        //     roles = user.roles()
        // }
    }


    fun findBy(id: UUID): UserEntity? {
        val test = UserEntity.findById(id)

        return test
    }

    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
