package io.easybreezy.user.application

import io.easybreezy.user.model.Users
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate

class Validation  {

    object Unique : Constraint {
        override val name: String
            get() = "User with this email already exist"
    }

    // private fun <E> Validator<E>.Property<String?>.isUnique(name: String): Validator<E>.Property<String?> =
    //     this.validate(Unique) { value ->
    //
    //         transaction {
    //             Users.select{ Users.email.address eq name }.
    //         }
    //         //
    //         // dataSource.jooqDSL { ctx ->
    //         //     !ctx.fetchExists(
    //         //         DSL.select()
    //         //             .from(USERS)
    //         //             .where(
    //         //                 DSL.field(name.toUpperCase()).eq(value)
    //         //             )
    //         //     )
    //         // }
    //     }

    fun onInvite(command: Invite) {
        validate(command) {
            validate(Invite::email).isNotBlank().isNotNull()
        }
    }
}
