package io.easybreezy.user.model_legacy

import java.util.UUID

typealias UserId = UUID

// class User(
//     private var roles: MutableSet<Role>,
//     private var status: Status
// ) : AggregateRoot() {
//
//     private val id: UserId = UserId.randomUUID()
//
//     private lateinit var name: Name
//     private lateinit var password: Password
//     private var token: String? = null
//
//     companion object {
//         fun create(email: Email, password: Password, name: Name, roles: MutableSet<Role>): User {
//             val user = User(email, roles, Status.ACTIVE)
//             user.name = name
//             user.password = password
//             user.addEvent(UserCreated(user.id))
//
//             return user
//         }
//
//         fun invite(email: Email, roles: MutableSet<Role>): User {
//             val user = User(email, roles, Status.WAIT_CONFIRM)
//             user.token = Token.generate()
//             user.addEvent(UserInvited(user.id))
//
//             return user
//         }
//     }
//
//     fun confirm(password: Password, name: Name) {
//         this.password = password
//         this.name = name
//         this.status = Status.ACTIVE
//         resetToken()
//
//         addEvent(UserConfirmed(id))
//     }
//
//     fun rename(name: Name) {
//         this.name = name
//     }
//
//     fun changePassword(password: Password) {
//         this.password = password
//     }
//
//     fun activate() {
//         this.status = Status.ACTIVE
//     }
//
//     private fun resetToken() {
//         token = null
//     }
//
//     enum class Status {
//         ACTIVE, WAIT_CONFIRM
//     }
//
//     enum class Role() {
//         ADMIN, MEMBER
//     }
// }
