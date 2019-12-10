package io.easybreezy.infrastructure.hibernate

import com.google.inject.Inject
import kotlinx.coroutines.coroutineScope
import org.hibernate.Session

class TransactionWrapper @Inject constructor(private val session: Session) {

    suspend operator fun invoke(block: suspend () -> Any): Any = coroutineScope {
        val transaction = session.beginTransaction()
        try {
            val result = block()
            transaction.commit()

            return@coroutineScope result
        } catch (ex: Throwable) {
            transaction.rollback()

            throw ex
        }
    }
}
