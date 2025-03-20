package org.ecwid.service

import org.ecwid.domain.core.AbstractQueryParser
import org.ecwid.query.models.*
import org.ecwid.query.types.QueryType

class SqlParserImpl : AbstractQueryParser() {

    override fun parseSql(sql: String): AbstractQuery {
//        Ideally we should handle all the possible sql query types like this, but for now lets deal with select type only
//
//        return when (QueryUtils.resolveQueryType(sql)) {
//            QueryType.SELECT -> parseSelect(sql)
//            QueryType.INSERT -> parseInsert(sql)
//            QueryType.UPDATE -> parseUpdate(sql)
//            QueryType.DELETE -> parseDelete(sql)
//        }

        return parse(sql, QueryType.SELECT)
    }

    private fun parse(sql: String, type: QueryType): AbstractQuery {
        val query = AbstractQuery(type = type)

        query.columns.addAll(this.extractColumns(sql))
        query.fromSource = this.extractFromSource(sql)
        query.joins?.addAll(this.extractJoins(sql))
        query.whereClause = this.extractWhereClause(sql)
        query.groupByColumns?.addAll(this.extractGroupBy(sql))
        query.orderByColumns?.addAll(this.extractOrderByColumns(sql))
        query.limit = this.extractLimits(sql)
        query.offset = this.extractOffset(sql)

        this.extractNestedQueries(sql).forEach {
            query.nestedQueries.add(parse(it, type))
        }

        return query
    }

    private fun parseSelect(sql: String): AbstractQuery {
        return AbstractQuery(type = QueryType.SELECT)
    }

    private fun parseDelete(sql: String): AbstractQuery {
        return AbstractQuery(type = QueryType.DELETE)
    }

    private fun parseUpdate(sql: String): AbstractQuery {
        return AbstractQuery(type = QueryType.UPDATE)
    }

    private fun parseInsert(sql: String): AbstractQuery {
        return AbstractQuery(type = QueryType.INSERT)
    }

    override fun toSql(query: AbstractQuery): String {
        // Here we can also convert our query class into raw sql, making our sql parser bi-directional
        return ""
    }
}