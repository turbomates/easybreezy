package io.easybreezy.infrastructure.query

import io.ktor.http.Parameters
import io.ktor.request.ApplicationRequest

fun ApplicationRequest.pagingParameters(maxPageSize: Int = 100): PagingParameters {
    return queryParameters.paging(maxPageSize)
}

fun Parameters.paging(maxPageSize: Int = 100, default: Int = 30): PagingParameters {
    val pageSize = this["pageSize"]?.toIntOrNull() ?: default
    val page = this["currentPage"]?.toIntOrNull() ?: 1

    return PagingParameters(
            if (pageSize > maxPageSize) maxPageSize else pageSize,
            page
    )
}

data class PagingParameters(val pageSize: Int, val currentPage: Int) {
    val offset = offset(pageSize, currentPage)
}

fun offset(pageSize: Int, currentPage: Int): Int {
    return pageSize * (currentPage - 1)
}
