package org.ecwid.query.parts

data class Join(
    var type: String? = null,
    var table: String,
    var condition: String
)