package org.ecwid.query.models

import org.ecwid.query.parts.Join
import org.ecwid.query.parts.Sort
import org.ecwid.query.parts.Source
import org.ecwid.query.parts.WhereClause
import org.ecwid.query.types.QueryType

data class AbstractQuery(
    var columns: MutableList<String> = ArrayList(),
    var fromSource: Source? = null,
    var joins: MutableList<Join>? = ArrayList(),
    var whereClause: WhereClause? = null,
    var groupByColumns: MutableList<String>? = ArrayList(),
    var orderByColumns: MutableList<Sort>? = ArrayList(),
    var nestedQueries: MutableList<AbstractQuery> = ArrayList(),
    var limit: Int? = null,
    var offset: Int? = null,
    var type: QueryType
)