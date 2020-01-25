package io.easybreezy.infrastructure.query

fun <T> List<T>.toContinuousList(pageSize: Int, currentPage: Int): ContinuousList<T> {
    val hasMore = size > pageSize
    val list = take(pageSize)
    return ContinuousList(list, pageSize, currentPage, hasMore)
}

class ContinuousList<T>(
    private val list: List<T>,
    val pageSize: Int,
    val currentPage: Int,
    val hasMore: Boolean = false
) : List<T> by list