package io.easybreezy.user.model

interface UserGateway {
    fun save(user: User)
    fun update()
}
