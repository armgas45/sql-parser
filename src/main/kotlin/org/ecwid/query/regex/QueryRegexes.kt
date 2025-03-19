package org.ecwid.query.regex

interface QueryRegexes {
    companion object {
        const val SELECT_REGEX = "SELECT\\s+(.*?)\\s+FROM"
        const val FROM_REGEX = "FROM\\s+(\\w+)(?:\\s+(?!WHERE|JOIN|GROUP|ORDER|LIMIT)(\\w+))?"
        const val JOIN_REGEX = "(LEFT JOIN|RIGHT JOIN|INNER JOIN|OUTER JOIN|JOIN)\\s+(\\w+)\\s+(\\w+)\\s+ON\\s+([^\\n]+)"
        const val WHERE_REGEX = "WHERE\\s+(.+?)\\s*(GROUP BY|ORDER BY|LIMIT|OFFSET|$)"
        const val GROUP_BY_REGEX = "GROUP BY\\s+(.+?)\\s*(ORDER BY|LIMIT|OFFSET|$)"
        const val ORDER_BY_REGEX = "ORDER BY\\s+(.+?)\\s*(LIMIT|OFFSET|$)"
        const val LIMIT_REGEX = "LIMIT\\s+(\\d+)"
        const val OFFSET_REGEX = "OFFSET\\s+(\\d+)"
        const val COMMA_REGEX = "\\s*,\\s*"
        const val DELETE_FROM_REGEX = "DELETE\\s+FROM\\s+([\\w.]+)"
        const val UPDATE_REGEX = "UPDATE\\s+([\\w.]+)(?:\\s+AS\\s+(\\w+))?"
        const val SET_REGEX = "SET\\s+(.*?)(?:\\s+WHERE|$)"
        const val INSERT_INTO_REGEX = "INSERT\\s+INTO\\s+([\\w.]+)(?:\\s+AS\\s+(\\w+))?"
        const val INSERT_COLUMNS_REGEX = "\\(([^)]+)\\)\\s+VALUES"
        const val INSERT_VALUES_REGEX = "VALUES\\s*\\((.*?)\\)\\s*;?"
        const val NESTED_QUERY_REGEX = "\\(\\s*(SELECT\\b.*?)(?=\\))"
    }
}