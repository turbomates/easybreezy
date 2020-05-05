package io.easybreezy.infrastructure.query

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import kotlinx.coroutines.runBlocking

class QueryExecutor @Inject constructor(private val transactional: TransactionManager) {
    suspend fun <T> execute(queryObject: QueryObject<T>): T {
        return transactional { queryObject.getData() }
    }

    @Deprecated("Temp method for regular valiktor functions")
    fun <T> executeSync(queryObject: QueryObject<T>): T = runBlocking {
        transactional.invoke { queryObject.getData() }
    }
}
