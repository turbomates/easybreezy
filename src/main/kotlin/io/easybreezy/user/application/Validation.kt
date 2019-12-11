package io.easybreezy.user.application

import com.google.inject.Inject
import io.easybreezy.application.db.jooq.jooqDSL
import io.easybreezy.tables.Users.USERS
import io.easybreezy.user.model.User
import org.jooq.impl.DSL
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate
import javax.sql.DataSource

class Validation @Inject constructor(private val dataSource: DataSource) {

    object Unique : Constraint {
        override val name: String
            get() = "User with this email already exist"
    }

    private fun <E> Validator<E>.Property<String?>.isUnique(name: String): Validator<E>.Property<String?> =
        this.validate(Unique) { value ->
            dataSource.jooqDSL { ctx ->
                !ctx.fetchExists(
                    DSL.select()
                        .from(USERS)
                        .where(
                            DSL.field(name.toUpperCase()).eq(value)
                        )
                )
            }
        }

    fun onInvite(command: Invite) {
        validate(command) {
            validate(Invite::email).isNotBlank().isNotNull().isUnique("email_address")
        }
    }
}
