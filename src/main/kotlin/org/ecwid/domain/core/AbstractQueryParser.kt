package org.ecwid.domain.core

import org.ecwid.query.parts.Join
import org.ecwid.query.parts.Sort
import org.ecwid.query.parts.Source
import org.ecwid.query.parts.WhereClause
import org.ecwid.query.regex.QueryRegexes
import org.ecwid.query.utils.RegexUtils.Companion.matches
import java.util.*
import java.util.regex.Pattern

abstract class AbstractQueryParser : SqlParser {

    protected fun extractColumns(sql: String): List<String> {
        val fieldMatcher = matches(
            sql,
            QueryRegexes.SELECT_REGEX,
            Pattern.CASE_INSENSITIVE or Pattern.DOTALL
        )

        if (fieldMatcher.find()) {
            val selectPart = fieldMatcher.group(1)
            return Arrays.stream(
                selectPart.split(QueryRegexes.COMMA_REGEX.toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray())
                .map { obj: String -> obj.trim { it <= ' ' } }
                .toList()
        }

        return emptyList()
    }

    protected fun extractFromSource(sql: String): Source? {
        val tableMatcher = matches(sql, QueryRegexes.FROM_REGEX, Pattern.CASE_INSENSITIVE)

        if (tableMatcher.find()) {
            val table = tableMatcher.group(1)
            val alias = tableMatcher.group(2)

            return Source(table, alias)
        }

        return null
    }

    protected fun extractJoins(sql: String): List<Join> {
        val joinMatcher = matches(sql, QueryRegexes.JOIN_REGEX, Pattern.CASE_INSENSITIVE)
        val joins = mutableListOf<Join>()

        while (joinMatcher.find()) {
            joins.add(
                Join(
                    type = joinMatcher.group(1),
                    table = joinMatcher.group(2),
                    condition = joinMatcher.group(4).trim()
                )
            )
        }

        return joins
    }

    protected fun extractWhereClause(sql: String): WhereClause? {
        val whereMatcher = matches(
            sql,
            QueryRegexes.WHERE_REGEX,
            Pattern.CASE_INSENSITIVE or Pattern.DOTALL
        )

        if (whereMatcher.find()) {
            return WhereClause(whereMatcher.group(1).trim())
        }

        return null
    }

    protected fun extractGroupBy(sql: String): List<String> {
        val groupByMatcher = matches(
            sql,
            QueryRegexes.GROUP_BY_REGEX,
            Pattern.CASE_INSENSITIVE
        )

        if (groupByMatcher.find()) {
            return Arrays.stream(groupByMatcher.group(1).split(QueryRegexes.COMMA_REGEX).toTypedArray())
                .map { obj: String -> obj.trim { it <= ' ' } }
                .toList()
        }

        return emptyList()
    }

    protected fun extractOrderByColumns(sql: String): MutableList<Sort> {
        val orderByMatcher = matches(sql, QueryRegexes.ORDER_BY_REGEX, Pattern.CASE_INSENSITIVE)
        val sortColumns = mutableListOf<Sort>()

        if (orderByMatcher.find()) {
            val orderClauses: Array<String> = orderByMatcher.group(1).split(QueryRegexes.COMMA_REGEX).toTypedArray()
            for (clause in orderClauses) {
                val parts = clause.trim { it <= ' ' }.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val column = parts[0]
                val direction = if (parts.size > 1) parts[1] else "ASC"

                sortColumns.add(Sort(column, direction))
            }
        }

        return sortColumns
    }

    protected fun extractNestedQueries(sql: String): MutableList<String> {
        val nestedQueryMatcher = matches(sql, QueryRegexes.NESTED_QUERY_REGEX, Pattern.CASE_INSENSITIVE)
        val nestedQueries: MutableList<String> = mutableListOf()

        while (nestedQueryMatcher.find()) {
            val nestedQuery = nestedQueryMatcher.group(1)
            nestedQueries.add(nestedQuery)
        }

        return nestedQueries
    }

    protected fun extractLimits(sql: String): Int? {
        val limitMatcher = matches(sql, QueryRegexes.LIMIT_REGEX, Pattern.CASE_INSENSITIVE)
        if (limitMatcher.find()) {
            return limitMatcher.group(1).toInt()
        }

        return null
    }

    protected fun extractOffset(sql: String): Int? {
        val offsetMatcher = matches(sql, QueryRegexes.OFFSET_REGEX, Pattern.CASE_INSENSITIVE)
        if (offsetMatcher.find()) {
            return offsetMatcher.group(1).toInt()
        }

        return null
    }
}