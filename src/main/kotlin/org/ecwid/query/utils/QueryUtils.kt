package org.ecwid.query.utils

import org.ecwid.query.types.QueryType

class QueryUtils {

    companion object {
        fun resolveQueryType(query: String): QueryType {
            val normalizedQuery = query.trim().uppercase()

            return when {
                normalizedQuery.startsWith("SELECT") -> QueryType.SELECT
                normalizedQuery.startsWith("INSERT") -> QueryType.INSERT
                normalizedQuery.startsWith("DELETE") -> QueryType.DELETE
                normalizedQuery.startsWith("UPDATE") -> QueryType.UPDATE
                else -> throw IllegalArgumentException("Unsupported operation: $query")
            }
        }
    }
}