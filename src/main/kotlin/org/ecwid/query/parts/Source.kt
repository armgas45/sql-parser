package org.ecwid.query.parts

data class Source (
    var tableName: String,
    var alias: String? = null
)