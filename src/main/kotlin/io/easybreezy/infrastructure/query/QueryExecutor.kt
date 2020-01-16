package io.easybreezy.infrastructure.query

class QueryExecutor {
    fun <T> execute(queryObject: QueryObject<T>): T {
        return queryObject.getData()
    }
}
