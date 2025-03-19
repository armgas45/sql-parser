package org.ecwid.domain.core

import org.ecwid.query.models.AbstractQuery

interface SqlParser {
    fun parseSql(sql: String): AbstractQuery
    fun toSql(query: AbstractQuery): String
}